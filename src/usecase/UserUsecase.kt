package com.example.usecase

import com.example.domain.*

interface UserUsecase {
    fun get(id: UserId): User
}