package com.mark.cafebuddy.security.filter

import com.mark.cafebuddy.security.authentication.JwtAuthenticationToken
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher

class JwtAuthenticationFilter(
    requestMatcher: RequestMatcher,
    authenticationManager: AuthenticationManager
) : AbstractAuthenticationProcessingFilter(requestMatcher, authenticationManager) {

    companion object {
        private const val JWT_TOKEN_PREFIX = "Bearer "
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val jwtToken = resolveJwtToken(request!!)
        val jwtAuthenticationToken: JwtAuthenticationToken = JwtAuthenticationToken.unauthenticated(jwtToken)
        return authenticationManager.authenticate(jwtAuthenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        SecurityContextHolder.getContext().authentication = authResult
        chain!!.doFilter(request, response)
    }

    private fun resolveJwtToken(request: HttpServletRequest): String {
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authorizationHeader != null && authorizationHeader.isNotBlank() &&
            authorizationHeader.startsWith(JWT_TOKEN_PREFIX)
        ) {
            return authorizationHeader.substring(JWT_TOKEN_PREFIX.length)
        }
        throw BadCredentialsException("Invalid authorizationHeader: $authorizationHeader")
    }
}