package com.example.usecase

import com.example.domain.CommentId
import com.example.gateway.MessagePort
import com.example.gateway.UserNotFoundException
import com.example.gateway.UserPort
import com.example.valueobject.CreatedComment

class CommentCreateUseCaseImpl(private val messagePort: MessagePort, private val userPort: UserPort) :
    CommentCreateUseCase {
    override fun execute(comment: CreatedComment): CommentId {
        if (userPort.isNotFound(comment.userId)) throw UserNotFoundException(comment.userId)
        return messagePort.createComment(comment)
    }
}