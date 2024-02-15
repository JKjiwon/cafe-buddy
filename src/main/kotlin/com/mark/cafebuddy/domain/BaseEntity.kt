package com.mark.cafebuddy.domain

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(

    @CreatedDate
    var createdDateTime: LocalDateTime? = LocalDateTime.now(),

    @LastModifiedDate
    var modifiedDateTime: LocalDateTime? = LocalDateTime.now()
)