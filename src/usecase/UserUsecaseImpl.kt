package com.example.usecase

import com.example.domain.User
import com.example.domain.UserId
import com.example.gateway.UserPort
import com.example.gateway.UserRepository

class UserUsecaseImpl(private val repository: UserPort) : UserUsecase {
    override fun get(id: UserId): User = repository.findBy(id)
}