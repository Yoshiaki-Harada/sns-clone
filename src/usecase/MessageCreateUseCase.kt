package com.example.usecase

import com.example.domain.MessageId
import com.example.valueobject.CreatedMessage

interface MessageCreateUseCase {
    fun execute(message: CreatedMessage): MessageId
}