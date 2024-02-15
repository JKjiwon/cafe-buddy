package com.mark.cafebuddy.domain.account.service

import com.mark.cafebuddy.common.exception.ApplicationException
import com.mark.cafebuddy.common.exception.ErrorCode
import com.mark.cafebuddy.domain.account.dto.AccountDetails
import com.mark.cafebuddy.domain.account.dto.SignInRequest
import com.mark.cafebuddy.domain.account.dto.SignUpRequest
import com.mark.cafebuddy.domain.account.entity.Account
import com.mark.cafebuddy.domain.account.repository.AccountRepository
import com.mark.cafebuddy.security.utils.jwt.JwtToken
import com.mark.cafebuddy.security.utils.jwt.JwtUtils
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils,
    private val authenticationManager: AuthenticationManager
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

    fun signIn(request: SignInRequest): JwtToken {
        val authentication: Authentication = try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.phoneNumber,
                    request.password
                )
            )
        } catch (authenticationManager: AuthenticationException) {
            throw ApplicationException(ErrorCode.UNAUTHORIZED)
        }
        SecurityContextHolder.getContext().authentication = authentication

        val account = accountRepository.findByPhoneNumberWithRoles(request.phoneNumber)!!
        return jwtUtils.create(account.id!!)
    }

    @Transactional(readOnly = true)
    fun me(accountId: Long): AccountDetails {
        val account = accountRepository.findById(accountId)
            .orElseThrow { ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR) }

        return AccountDetails(phoneNumber = account.phoneNumber, name = account.name)
    }
}