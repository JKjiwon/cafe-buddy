package com.mark.cafebuddy.config.security

import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

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
                    .requestMatchers("/api/**").authenticated()
                    .requestMatchers("/open-api/**").permitAll()
            }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            .build()
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
}