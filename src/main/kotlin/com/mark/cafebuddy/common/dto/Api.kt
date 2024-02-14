package com.mark.cafebuddy.common.dto

import com.mark.cafebuddy.common.exception.ErrorCode

data class Api<T>(
    val meta: MetaData,
    val data: T? = null
) {
    companion object {
        fun <T> of(errorCode: ErrorCode, data: T? = null): Api<T> =
            Api(MetaData(errorCode.code, errorCode.message), data)

        fun <T> ok(data: T? = null): Api<T> = of(ErrorCode.OK, data)
    }
}


data class MetaData(
    val code: Int,
    val message: String
)