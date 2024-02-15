package com.mark.cafebuddy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class CafeBuddyApplication

fun main(args: Array<String>) {
    runApplication<CafeBuddyApplication>(*args)
}
