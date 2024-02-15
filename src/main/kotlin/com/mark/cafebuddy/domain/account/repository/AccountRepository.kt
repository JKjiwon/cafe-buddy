package com.mark.cafebuddy.domain.account.repository

import com.mark.cafebuddy.domain.account.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    @Query("select a from Account a join fetch a.roles where a.id = :id")
    fun findByIdWithRoles(id: Long): Account?

    @Query("select a from Account a join fetch a.roles where a.phoneNumber = :phoneNumber")
    fun findByPhoneNumberWithRoles(phoneNumber: String): Account?

    fun existsByPhoneNumber(phoneNumber: String): Boolean
}