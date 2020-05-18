package com.example.gateway

import com.example.domain.User
import com.example.domain.UserId
import com.example.domain.Users

interface UserPort {
    fun find(): Users
    fun findBy(id: UserId): User
    fun isFound(id: UserId): Boolean
    fun isNotFound(id: UserId): Boolean
    fun create(user: User):UserId
    fun update(user: User)
}