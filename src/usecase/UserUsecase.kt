package com.example.usecase

import com.example.domain.User
import com.example.domain.UserId
import com.example.domain.Users

interface UserUsecase {
    fun get(): Users
    fun get(id: UserId): User
    fun create(user: User): UserId
    fun update(user: User)
}