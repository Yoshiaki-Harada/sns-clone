package com.example.usecase

import com.example.domain.MessageId
import com.example.valueobject.CreatedMessage

interface MessageUpdateUseCase {
    fun execute(id: MessageId, message: CreatedMessage)
}