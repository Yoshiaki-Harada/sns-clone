package com.example.usecase

import com.example.domain.MessageId
import com.example.gateway.MessagePort
import com.example.gateway.UserNotFoundException
import com.example.gateway.UserPort
import com.example.valueobject.CreatedMessage

class MessageUpdateUseCaseImpl(private val messagePort: MessagePort, private val userPort: UserPort) :
    MessageUpdateUseCase {
    override fun execute(id: MessageId, message: CreatedMessage) {
        if (userPort.isNotFound(message.userId)) throw UserNotFoundException(message.userId)
        messagePort.updateMessage(id, message)
    }
}