package com.example.usecase

import com.example.domain.UserId
import com.example.valueobject.CreatedUser

interface UserUpdateUseCase {
    fun execute(userId: UserId, user: CreatedUser)
}