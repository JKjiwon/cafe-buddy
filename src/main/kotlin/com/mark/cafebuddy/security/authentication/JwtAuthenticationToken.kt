package com.mark.cafebuddy.security.authentication

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class JwtAuthenticationToken(
    val jwtToken: String? = null,
    val principal: UserDetails? = null,
    authorities: MutableCollection<out GrantedAuthority>? = null
) : AbstractAuthenticationToken(authorities) {

    init {
        if (!authorities.isNullOrEmpty()) {
            isAuthenticated = true
        }
    }

    companion object {
        @JvmStatic
        fun unauthenticated(jwtToken: String): JwtAuthenticationToken {
            return JwtAuthenticationToken(jwtToken = jwtToken)
        }

        @JvmStatic
        fun authenticated(
            principal: UserDetails,
            authorities: MutableCollection<out GrantedAuthority>
        ): JwtAuthenticationToken {
            return JwtAuthenticationToken(principal = principal, authorities = authorities)
        }
    }

    override fun getCredentials(): Any? {
        return jwtToken
    }

    override fun getPrincipal(): Any? {
        return principal
    }

}