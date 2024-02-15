package com.mark.cafebuddy.security.utils.jwt

import java.time.LocalDateTime

data class JwtToken(
    val accessToken: String,
    val expiration: LocalDateTime
)