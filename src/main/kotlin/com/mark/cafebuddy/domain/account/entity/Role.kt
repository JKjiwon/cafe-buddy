package com.mark.cafebuddy.domain.account.entity

enum class Role(
    val description: String
) {
    USER("일반 계정"),
    ADMIN("어드민 계정")
}