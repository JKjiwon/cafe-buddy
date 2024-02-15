package com.mark.cafebuddy.domain.account.dto

data class SignInRequest(
    val phoneNumber: String,
    val password: String,
)
