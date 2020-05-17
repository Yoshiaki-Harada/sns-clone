package com.example.usecase

import com.example.domain.User
import com.example.domain.UserId

interface UserUsecase {
    fun get(id :UserId) :User
}