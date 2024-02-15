package com.mark.cafebuddy.config.security

import com.mark.cafebuddy.security.exceptionhandler.CustomAccessDenierHandler
import com.mark.cafebuddy.security.exceptionhandler.CustomAuthenticationEntryPoint
import com.mark.cafebuddy.security.filter.JwtAuthenticationFilter
import com.mark.cafebuddy.security.provider.JwtAuthenticationProvider
import com.mark.cafebuddy.security.userdetails.AccountUserDetailService
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher


@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val customAccessDenierHandler: CustomAccessDenierHandler,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val accountUserDetailService: AccountUserDetailService
) {

    companion object {
        val SWAGGER_REQUEST_MATCHER: RequestMatcher = OrRequestMatcher(
            AntPathRequestMatcher("/v3/api-docs/**"),
            AntPathRequestMatcher("/swagger-ui/**"),
            AntPathRequestMatcher("/swagger-resources/**")
        )
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { csrf -> csrf.disable() }
            .cors { cors -> cors.disable() }
            .formLogin { formLogin -> formLogin.disable() }
            .authorizeHttpRequests { authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers("/open-api/**").permitAll()
                    .requestMatchers("/api/**").authenticated()
            }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.accessDeniedHandler(customAccessDenierHandler)
                it.authenticationEntryPoint(customAuthenticationEntryPoint)
            }
            .build()
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return ProviderManager(jwtAuthenticationProvider, daoAuthenticationProvider())
    }

    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(AntPathRequestMatcher("/api/**"), authenticationManager())
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers(SWAGGER_REQUEST_MATCHER)
                .requestMatchers(PathRequest.toH2Console())
        }
    }

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val daoAuthenticationProvider = DaoAuthenticationProvider()
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder())
        daoAuthenticationProvider.setUserDetailsService(accountUserDetailService)
        return daoAuthenticationProvider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}