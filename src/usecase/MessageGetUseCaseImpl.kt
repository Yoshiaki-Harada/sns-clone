package com.example.usecase

import com.example.domain.Message
import com.example.domain.MessageId
import com.example.gateway.MessagePort
import com.example.gateway.UserPort

class MessageGetUseCaseImpl(private val messagePort: MessagePort):MessageGetUseCase {
    override fun execute(id: MessageId): Message = messagePort.getMessage(id)
}