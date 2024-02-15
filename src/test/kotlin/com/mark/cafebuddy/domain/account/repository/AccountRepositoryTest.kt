package com.mark.cafebuddy.domain.account.repository

import com.mark.cafebuddy.domain.account.entity.Account
import com.mark.cafebuddy.domain.account.entity.Role
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Test
    @DisplayName("계정 Id로 계정(Account)을 조회 할 수 있다")
    fun findById() {
        val account1 = Account(
            phoneNumber = "01011112222",
            name = "홍길동",
            password = "1234",
            roles = listOf(Role.USER, Role.ADMIN)
        )

        val account2 = Account(
            phoneNumber = "01022224444",
            name = "김철수",
            password = "1234",
            roles = listOf(Role.USER)
        )
        val savedAccount1 = accountRepository.save(account1)
        val savedAccount2 = accountRepository.save(account2)

        val foundAccount = accountRepository.findByIdWithRoles(savedAccount2.id!!)
        assertThat(foundAccount)
            .extracting("name", "phoneNumber")
            .contains("김철수", "01022224444")
    }

    @Test
    @DisplayName("계정 Id로 계정(Account) 조회 시 권한(Role)도 함께 조회한다.")
    fun findByIdWithRoles() {
        // given
        val account = Account(
            phoneNumber = "01011112222",
            name = "홍길동",
            password = "1234",
            roles = listOf(Role.USER, Role.ADMIN)
        )
        val savedAccount = accountRepository.save(account)

        // when
        val foundAccount = accountRepository.findByIdWithRoles(savedAccount.id!!)

        // then
        assertThat(foundAccount!!.roles)
            .hasSize(2)
            .contains(Role.USER, Role.ADMIN)
    }

    @Test
    @DisplayName("핸드폰 번호로 계정이 중복인지 확인 할 수 있다")
    fun existsByPhoneNumber() {
        // given
        val phoneNumber = "01011112222"
        val account = Account(
            phoneNumber = phoneNumber,
            name = "홍길동",
            password = "1234",
            roles = listOf(Role.USER, Role.ADMIN)
        )
        accountRepository.save(account)

        // when
        val exists = accountRepository.existsByPhoneNumber(phoneNumber)
        val notExists = accountRepository.existsByPhoneNumber("01022223333")

        // then
        assertThat(exists).isTrue()
        assertThat(notExists).isFalse()
    }
}