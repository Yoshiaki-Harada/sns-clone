package com.example.usecase

import com.example.domain.*

interface UserUsecase {
    fun get(id: UserId): User
    fun create(user: User): UserId
    fun update(user: User)
}