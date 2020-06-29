package com.example.usecase

import com.example.domain.By
import com.example.domain.Messages
import com.example.domain.Order
import com.example.domain.UserId
import com.example.gateway.MessagePort

class MessagesGetUseCaseImpl(private val messagePort: MessagePort) : MessagesGetUseCase{
    override fun execute(by: By, order: Order): Messages =
        messagePort.getMessages().sorted(by, order).sortedComments(by, order)

    override fun execute(userId: UserId, by: By, order: Order): Messages =
        messagePort.getMessages(userId).sorted(by, order).sortedComments(by, order)
}