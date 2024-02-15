package com.mark.cafebuddy.security.userdetails

import com.mark.cafebuddy.domain.account.repository.AccountRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AccountUserDetailService(
    private val accountRepository: AccountRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val account = accountRepository.findByPhoneNumberWithRoles(username!!)
            ?: throw UsernameNotFoundException("Account Not found")

        return AccountUserDetails(account)
    }

}