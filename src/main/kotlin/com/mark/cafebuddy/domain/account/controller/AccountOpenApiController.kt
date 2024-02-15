package com.mark.cafebuddy.domain.account.controller

import com.mark.cafebuddy.common.dto.Api
import com.mark.cafebuddy.domain.account.dto.AccountDetails
import com.mark.cafebuddy.domain.account.dto.SignInRequest
import com.mark.cafebuddy.domain.account.dto.SignUpRequest
import com.mark.cafebuddy.domain.account.service.AccountService
import com.mark.cafebuddy.security.utils.jwt.JwtToken
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/open-api/accounts")
class AccountOpenApiController(
    private val accountService: AccountService
) {
    @PostMapping
    fun signUp(@Valid @RequestBody request: SignUpRequest): Api<AccountDetails> {
        val response = accountService.signUp(request)
        return Api.ok(response)
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody request: SignInRequest): Api<JwtToken> {
        val response = accountService.signIn(request)
        return Api.ok(response)
    }
}