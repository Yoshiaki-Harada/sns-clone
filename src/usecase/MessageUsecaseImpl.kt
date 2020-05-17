package com.example.usecase

import com.example.domain.By
import com.example.domain.Messages
import com.example.domain.Order
import com.example.domain.UserId
import com.example.gateway.MessagePort

class MessageUsecaseImpl(private val messagePort: MessagePort) : MessageUsecase {
    override fun get(by: By, order: Order): Messages {
        return messagePort.getMessages().sorted(by, order).sortedComments(by, order)
    }

    override fun getByUerId(userId: UserId, by: By, order: Order): Messages {
        return messagePort.getMessages(userId).sorted(by, order).sortedComments(by, order)
    }
}