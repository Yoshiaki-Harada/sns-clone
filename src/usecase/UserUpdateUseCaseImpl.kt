package com.example.usecase

import com.example.domain.UserId
import com.example.gateway.UserPort
import com.example.valueobject.CreatedUser

class UserUpdateUseCaseImpl(private val repository: UserPort) : UserUpdateUseCase {
    override fun execute(userId: UserId, user: CreatedUser) {
        repository.update(userId, user)
    }
}