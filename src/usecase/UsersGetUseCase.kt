package com.example.usecase

import com.example.domain.Users

interface UsersGetUseCase {
    fun execute(): Users
}