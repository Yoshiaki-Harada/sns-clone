package com.example.usecase

import com.example.domain.User
import com.example.domain.UserId

interface UserGetUseCase {
    fun execute(id: UserId): User
}