package com.example.usecase

import com.example.domain.UserId
import com.example.gateway.UserPort
import com.example.valueobject.CreatedUser

class UserCreateUseCaseImpl(private val repository: UserPort) :UserCreateUseCase{
    override fun execute(user: CreatedUser): UserId = repository.create(user)
}