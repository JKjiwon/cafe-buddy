package com.mark.cafebuddy.domain.account.service

import com.mark.cafebuddy.common.exception.ApplicationException
import com.mark.cafebuddy.domain.account.dto.SignUpRequest
import com.mark.cafebuddy.domain.account.repository.AccountRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AccountServiceTest {

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var accountRepository: AccountRepository

    @AfterEach
    fun tearDown() {
        accountRepository.deleteAllInBatch()
    }

    @Test
    @DisplayName("휴대폰 번호, 비밀번호, 이름으로 회원 가입을 할 수 있다.")
    fun signUp() {
        // given
        val signUpRequest = SignUpRequest(name = "홍길동", phoneNumber = "01022223333", password = "1234")

        // when
        val accountDetails = accountService.signUp(signUpRequest)

        // then
        assertThat(accountDetails.name).isEqualTo("홍길동")
        assertThat(accountDetails.phoneNumber).isEqualTo("01022223333")
    }

    @Test
    @DisplayName("휴대폰 번호가 중복이면 회원 가입을 할 수 없다.(휴대폰 번호는 010으로 시작하고 11자리 숫자를 가진다.)")
    fun failToSignUpBecauseOfDuplicatedPhoneNumber() {
        // given
        val phoneNumber = "01022223333"
        val signUpRequest = SignUpRequest(name = "홍길동", phoneNumber = phoneNumber, password = "1234")
        accountService.signUp(signUpRequest)

        // when & then
        val signUpRequest2 = SignUpRequest(name = "김철수", phoneNumber = phoneNumber, password = "12345")
        assertThatThrownBy { accountService.signUp(signUpRequest2) }
            .isInstanceOf(ApplicationException::class.java)
            .hasMessage("Already registered phone number")
    }

}