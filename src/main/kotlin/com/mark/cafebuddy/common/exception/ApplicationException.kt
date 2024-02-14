package com.mark.cafebuddy.common.exception

class ApplicationException(
    val errorCode: ErrorCode,
    cause: Throwable? = null
) : RuntimeException(errorCode.message, cause)