package com.mark.cafebuddy.security.exceptionhandler

import com.fasterxml.jackson.databind.ObjectMapper
import com.mark.cafebuddy.common.dto.Api
import com.mark.cafebuddy.common.exception.ErrorCode
import com.mark.cafebuddy.common.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDenierHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {

    val log = logger();
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        log.error("Authorization error", accessDeniedException)

        val forbidden = ErrorCode.FORBIDDEN
        response.status = forbidden.httpStatus.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val jsonResponse = Api.of(errorCode = forbidden, data = null)

        response.writer.write(objectMapper.writeValueAsString(jsonResponse))
    }
}