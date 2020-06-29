package com.example.usecase

import com.example.domain.CommentId
import com.example.gateway.MessageNotFoundException
import com.example.gateway.MessagePort
import com.example.gateway.UserNotFoundException
import com.example.gateway.UserPort
import com.example.valueobject.CreatedComment

class CommentUpdateUseCaseImpl(private val messagePort: MessagePort, private val userPort: UserPort) :
    CommentUpdateUseCase {
    override fun execute(id: CommentId, comment: CreatedComment) {
        if (userPort.isNotFound(comment.userId)) throw UserNotFoundException(comment.userId)
        if (messagePort.isNotFound(comment.messageId)) throw MessageNotFoundException(comment.messageId)
        messagePort.updateComment(
            id,
            comment
        )
    }
}