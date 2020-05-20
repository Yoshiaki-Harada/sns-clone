package com.example.usecase

import com.example.domain.User
import com.example.domain.UserId
import com.example.domain.Users
import com.example.valueobject.CreatedUser

interface UserUsecase {
    fun get(): Users
    fun get(id: UserId): User
    fun create(user: CreatedUser): UserId
    fun update(userId: UserId, user: CreatedUser)
}