package com.mark.cafebuddy.domain.account.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mark.cafebuddy.domain.account.dto.AccountDetails
import com.mark.cafebuddy.domain.account.dto.SignUpRequest
import com.mark.cafebuddy.domain.account.service.AccountService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@AutoConfigureMockMvc
@SpringBootTest
class AccountOpenApiControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var accountService: AccountService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @WithAnonymousUser
    @DisplayName("계정을 생성한다.")
    @Test
    fun signUp() {
        // given
        val signUpRequest = SignUpRequest(phoneNumber = "01022223333", password = "1234", name = "홍길동")

        val accountDetails = AccountDetails(phoneNumber = "01022223333", name = "홍길동")
        Mockito.`when`(accountService.signUp(signUpRequest)).thenReturn(accountDetails)

        // when & then
        mockMvc.perform(
            post("/open-api/accounts")
                .content(objectMapper.writeValueAsString(signUpRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.meta.code").value(200))
            .andExpect(jsonPath("$.meta.message").value("OK"))
            .andExpect(jsonPath("$.data.phoneNumber").value("01022223333"))
            .andExpect(jsonPath("$.data.name").value("홍길동"))
    }

    @WithAnonymousUser
    @DisplayName("회원 가입시 유효한 핸드폰 번호를 입력해야 한다.")
    @Test
    fun failToSignUpBecauseOfInvalidPhoneNumber() {
        // given
        val signUpRequest = SignUpRequest(phoneNumber = "010121233333", password = "1234", name = "홍길동")

        // when & then
        mockMvc.perform(
            post("/open-api/accounts")
                .content(objectMapper.writeValueAsString(signUpRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isPreconditionFailed)
            .andExpect(jsonPath("$.meta.code").value(412))
            .andExpect(jsonPath("$.meta.message").value("Invalid Request Value"))
            .andExpect(jsonPath("$.data[0].field").value("phoneNumber"))
            .andExpect(jsonPath("$.data[0].value").value("010121233333"))
            .andExpect(jsonPath("$.data[0].reason").value("invalid phone number"))
    }
}