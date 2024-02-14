package com.mark.cafebuddy.common.controller

import com.mark.cafebuddy.common.dto.Api
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@Tag(name = "Health Check", description = "Health Check API")
@RestController
@RequestMapping("/open-api/health")
class HealthController {

    @Operation(summary = "check", description = "check")
    @ApiResponses(ApiResponse(responseCode = "200", description = "OK"))
    @GetMapping("/check")
    fun check(): Api<Any> {
        return Api.ok()
    }
}

