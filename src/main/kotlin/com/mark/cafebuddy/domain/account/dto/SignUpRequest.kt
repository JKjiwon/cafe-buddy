package com.mark.cafebuddy.domain.account.dto

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SignUpRequest(

    @field:Pattern(regexp = "010\\d{8}", message = "invalid phone number")
    val phoneNumber: String,
    @field:Size(min = 1, max = 20)
    val password: String,
    @field:Size(min = 1, max = 20)
    val name: String

)
