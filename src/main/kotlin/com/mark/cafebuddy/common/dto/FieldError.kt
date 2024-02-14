package com.mark.cafebuddy.common.dto

data class FieldError(
    val field: String,
    val value: Any? = null,
    val reason: String,
)