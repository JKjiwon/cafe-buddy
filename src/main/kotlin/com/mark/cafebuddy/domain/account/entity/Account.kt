package com.mark.cafebuddy.domain.account.entity

import com.mark.cafebuddy.domain.BaseEntity
import jakarta.persistence.*


@Entity
@Table(name = "ACCOUNT")
class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true)
    var phoneNumber: String,

    var name: String,

    var password: String,

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "ACCOUNT_ROLE",
        joinColumns = [JoinColumn(name = "account_id")]
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    val roles: List<Role> = mutableListOf(Role.USER)
) : BaseEntity()

