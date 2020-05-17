package com.example.usecase

import com.example.domain.User
import com.example.domain.UserId
import com.example.gateway.UserPort

class UserUsecaseImpl(private val repository: UserPort) : UserUsecase {
    override fun get(id: UserId): User = repository.findBy(id)
    override fun create(user: User) = repository.create(user)
    override fun update(user: User) {
        repository.update(user)
    }
}