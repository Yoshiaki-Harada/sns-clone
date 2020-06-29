package com.example.usecase

import com.example.domain.MessageId
import com.example.gateway.MessagePort
import com.example.gateway.UserNotFoundException
import com.example.gateway.UserPort
import com.example.valueobject.CreatedMessage
import mu.KotlinLogging

class MessageCreateUseCaseImpl(private val messagePort: MessagePort, private val userPort: UserPort) :
    MessageCreateUseCase {
    val logger = KotlinLogging.logger {}
    override fun execute(message: CreatedMessage): MessageId {
        logger.debug { "create message" }
        if (userPort.isNotFound(message.userId)) throw UserNotFoundException(message.userId)
        return messagePort.createMessage(message)
    }
}