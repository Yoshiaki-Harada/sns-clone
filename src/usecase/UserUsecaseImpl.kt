package com.example.usecase

import com.example.domain.User
import com.example.domain.UserId
import com.example.gateway.UserPort
import com.example.valueobject.CreatedUser

class UserUsecaseImpl(private val repository: UserPort) : UserUsecase {
    override fun get() = repository.find()
    override fun get(id: UserId): User = repository.findBy(id)
    override fun create(user: CreatedUser) = repository.create(user)
    override fun update(userId: UserId, user: CreatedUser) {
        repository.update(userId, user)
    }
}