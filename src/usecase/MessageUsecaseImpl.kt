package com.example.usecase

import com.example.domain.*
import com.example.gateway.MessagePort
import com.example.gateway.UserNotFoundException
import com.example.gateway.UserPort
import com.example.valueobject.CreatedMessage

class MessageUsecaseImpl(private val messagePort: MessagePort, private val userPort: UserPort) : MessageUsecase {
    override fun get(by: By, order: Order): Messages {
        return messagePort.getMessages().sorted(by, order).sortedComments(by, order)
    }

    override fun getByUerId(userId: UserId, by: By, order: Order): Messages {
        return messagePort.getMessages(userId).sorted(by, order).sortedComments(by, order)
    }

    override fun create(message: CreatedMessage): MessageId {
        if (userPort.isFound(message.userId)) throw UserNotFoundException(message.userId)
        return messagePort.createMessage(message)
    }

    override fun updated(id: MessageId, message: CreatedMessage) {
        if (userPort.isNotFound(message.userId)) throw UserNotFoundException(message.userId)
        messagePort.updateMessage(id, message)
    }
}