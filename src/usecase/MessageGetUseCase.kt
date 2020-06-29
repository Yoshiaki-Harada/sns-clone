package com.example.usecase

import com.example.domain.*

interface MessageGetUseCase {
    fun execute(id: MessageId): Message
}