package com.example.gateway

import com.example.domain.User
import com.example.domain.UserId

interface UserPort {
    fun findBy(id: UserId): User
}