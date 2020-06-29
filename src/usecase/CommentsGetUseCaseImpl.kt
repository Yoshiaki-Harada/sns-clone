package com.example.usecase

import com.example.domain.By
import com.example.domain.Comments
import com.example.domain.MessageId
import com.example.domain.Order
import com.example.gateway.MessagePort

class CommentsGetUseCaseImpl(private val messagePort: MessagePort) : CommentsGetUseCase {
    override fun execute(id: MessageId, by: By, order: Order): Comments = messagePort.getComments(id).sorted(by, order)
}