package com.example.usecase

import com.example.domain.By
import com.example.domain.Messages
import com.example.domain.Order
import com.example.domain.UserId

interface MessagesGetUseCase {
    fun execute(by: By, order: Order): Messages
    fun execute(userId: UserId, by: By, order: Order): Messages
}