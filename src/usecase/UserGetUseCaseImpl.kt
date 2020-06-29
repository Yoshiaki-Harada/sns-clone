package com.example.usecase

import com.example.domain.User
import com.example.domain.UserId
import com.example.gateway.UserPort

class UserGetUseCaseImpl(private val repository: UserPort) :UserGetUseCase{
    override fun execute(id: UserId): User = repository.findBy(id)
}