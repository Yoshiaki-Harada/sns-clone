package com.example.usecase

import com.example.domain.UserId
import com.example.domain.Users
import com.example.valueobject.CreatedUser

interface UserCreateUseCase {
    fun execute(user: CreatedUser): UserId
}