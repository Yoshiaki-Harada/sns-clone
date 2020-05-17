package com.example.usecase

import com.example.domain.*
import com.example.gateway.UserPort

class UserUsecaseImpl(private val repository: UserPort) : UserUsecase {
    override fun get(id: UserId): User = repository.findBy(id)
}