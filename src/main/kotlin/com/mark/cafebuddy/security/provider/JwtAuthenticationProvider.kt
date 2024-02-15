package com.mark.cafebuddy.security.provider

import com.mark.cafebuddy.domain.account.repository.AccountRepository
import com.mark.cafebuddy.security.authentication.JwtAuthenticationToken
import com.mark.cafebuddy.security.userdetails.AccountUserDetails
import com.mark.cafebuddy.security.utils.jwt.JwtUtils
import com.mark.cafebuddy.security.utils.jwt.JwtVerificationResponse
import io.jsonwebtoken.JwtException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component


@Component
class JwtAuthenticationProvider(
    private val jwtUtils: JwtUtils,
    private val accountRepository: AccountRepository
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication?): Authentication {
        val jwtAuthentication = authentication as JwtAuthenticationToken

        val verifyResponse: JwtVerificationResponse
        try {
            verifyResponse = jwtUtils.verify(jwtAuthentication.jwtToken!!)
        } catch (e: JwtException) {
            throw BadCredentialsException("Invalid jwtToken - accessToken: ${authentication.jwtToken}", e)
        }

        val userDetails = loadAccountById(verifyResponse.accountId)

        return JwtAuthenticationToken.authenticated(
            principal = userDetails,
            authorities = userDetails.authorities.map { SimpleGrantedAuthority("ROLE_$it") }.toMutableList()
        )
    }

    override fun supports(authentication: Class<*>): Boolean {
        return JwtAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    private fun loadAccountById(accountId: Long): UserDetails {
        val account =
            accountRepository.findByIdWithRoles(accountId) ?: throw UsernameNotFoundException("Account Not found")
        return AccountUserDetails(account)
    }
}