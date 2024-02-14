package com.mark.cafebuddy.common.exception

import com.mark.cafebuddy.common.dto.Api
import com.mark.cafebuddy.common.dto.FieldError
import com.mark.cafebuddy.common.logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApplicationExceptionHandler {

    private val log = logger()

    @ExceptionHandler(value = [ApplicationException::class])
    fun applicationException(exception: ApplicationException): ResponseEntity<Api<Any>> {
        log.error("ApplicationException", exception)

        val errorCode = exception.errorCode

        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(
                Api.of(errorCode = errorCode)
            )
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun methodArgumentNotValidException(exception: MethodArgumentNotValidException)
            : ResponseEntity<Api<List<FieldError>>> {
        val fieldErrors = exception.bindingResult.fieldErrors.map {
            FieldError(
                it.field, it.rejectedValue, it.defaultMessage!!
            )
        }

        val errorCode = ErrorCode.INVALID_REQUEST_ERROR

        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(
                Api.of(errorCode, fieldErrors)
            )
    }

    @ExceptionHandler(value = [Exception::class])
    fun globalException(exception: Exception): ResponseEntity<Api<Any>> {
        log.error("GlobalException", exception)

        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR

        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(
                Api.of(errorCode = errorCode)
            )
    }
}