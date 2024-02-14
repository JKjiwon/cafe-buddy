package com.mark.cafebuddy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CafeBuddyApplication

fun main(args: Array<String>) {
    runApplication<CafeBuddyApplication>(*args)
}
