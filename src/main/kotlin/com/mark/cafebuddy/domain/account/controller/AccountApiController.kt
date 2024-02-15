package com.mark.cafebuddy.domain.account.controller

import com.mark.cafebuddy.common.dto.Api
import com.mark.cafebuddy.domain.account.dto.AccountDetails
import com.mark.cafebuddy.domain.account.service.AccountService
import com.mark.cafebuddy.security.userdetails.AccountUserDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/accounts")
class AccountApiController(
    private val accountService: AccountService
) {
    @GetMapping("/me")
    fun me(@AuthenticationPrincipal principal: AccountUserDetails): Api<AccountDetails> {
        val response = accountService.me(principal.account.id!!)
        return Api.ok(response)
    }
}