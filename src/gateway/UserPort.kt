package com.example.gateway

import com.example.domain.User
import com.example.domain.UserId
import com.example.domain.Users
import com.example.valueobject.CreatedUser

interface UserPort {
    fun find(): Users
    fun findBy(id: UserId): User
    fun isFound(id: UserId): Boolean
    fun isNotFound(id: UserId): Boolean
    fun create(user: CreatedUser):UserId
    fun update(userId: UserId, user: CreatedUser)
}