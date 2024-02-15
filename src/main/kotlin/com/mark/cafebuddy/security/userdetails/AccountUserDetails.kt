package com.mark.cafebuddy.security.userdetails

import com.mark.cafebuddy.domain.account.entity.Account
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

class AccountUserDetails(
    val account: Account
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return account.roles.stream().map { SimpleGrantedAuthority("ROLE_$it") }.collect(Collectors.toList())
    }

    override fun getPassword(): String {
        return account.password
    }

    override fun getUsername(): String {
        return account.phoneNumber
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}