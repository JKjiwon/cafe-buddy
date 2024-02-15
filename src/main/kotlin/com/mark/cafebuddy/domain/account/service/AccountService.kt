package com.mark.cafebuddy.domain.account.service

import com.mark.cafebuddy.common.exception.ApplicationException
import com.mark.cafebuddy.common.exception.ErrorCode
import com.mark.cafebuddy.domain.account.dto.AccountDetails
import com.mark.cafebuddy.domain.account.dto.SignUpRequest
import com.mark.cafebuddy.domain.account.entity.Account
import com.mark.cafebuddy.domain.account.repository.AccountRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun signUp(request: SignUpRequest): AccountDetails {
        val account = Account(
            phoneNumber = request.phoneNumber,
            password = passwordEncoder.encode(request.password),
            name = request.name
        )

        if (accountRepository.existsByPhoneNumber(request.phoneNumber)) {
            throw ApplicationException(ErrorCode.ALREADY_REGISTERED_PHONE_NUMBER)
        }

        val savedAccount = accountRepository.save(account)
        return AccountDetails(savedAccount.name, savedAccount.phoneNumber)
    }
}