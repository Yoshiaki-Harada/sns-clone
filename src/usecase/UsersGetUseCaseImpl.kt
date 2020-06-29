package com.example.usecase

import com.example.domain.Users
import com.example.gateway.UserPort

class UsersGetUseCaseImpl(private val repository: UserPort) :UsersGetUseCase{
    override fun execute(): Users = repository.find()
}