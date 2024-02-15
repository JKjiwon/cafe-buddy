package com.mark.cafebuddy.security.exceptionhandler

import com.fasterxml.jackson.databind.ObjectMapper
import com.mark.cafebuddy.common.dto.Api
import com.mark.cafebuddy.common.exception.ErrorCode
import com.mark.cafebuddy.common.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    val log = logger();
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        log.error("Authentication error", authException)

        val unauthorized = ErrorCode.UNAUTHORIZED
        response.status = unauthorized.httpStatus.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val jsonResponse = Api.of(errorCode = unauthorized, data = null)
        response.writer.write(objectMapper.writeValueAsString(jsonResponse))
    }
}