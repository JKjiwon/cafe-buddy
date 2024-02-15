package com.mark.cafebuddy.security.utils.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.security.Key
import java.time.ZoneId
import java.util.*

@Component
class JwtUtils(
    private val properties: JwtProperties
) {
    companion object {
        private const val CLAIM_ACCOUNT_ID_CODE = "accountId"
    }

    private var signingKey: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.secretKey))

    fun create(accountId: Long): JwtToken {
        val now = System.currentTimeMillis()
        val expiryDate = Date(now + properties.expirationMills)

        val claimsMap = mutableMapOf<String, String>()
        claimsMap[CLAIM_ACCOUNT_ID_CODE] = accountId.toString()

        val accessToken: String = Jwts.builder()
            .setIssuer(properties.issuer)
            .setClaims(claimsMap)
            .setIssuedAt(Date(now))
            .setExpiration(expiryDate)
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()

        return JwtToken(accessToken, expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
    }

    @Throws(
        JwtException::class
    )
    fun verify(accessToken: String): JwtVerificationResponse {
        val claims = Jwts.parserBuilder()
            .setSigningKey(signingKey).build().parseClaimsJws(accessToken).body

        return JwtVerificationResponse(
            accountId = claims[CLAIM_ACCOUNT_ID_CODE].toString().toLong()
        )
    }
}