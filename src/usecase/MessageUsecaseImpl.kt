package com.example.usecase

import com.example.domain.*
import com.example.gateway.MessageNotFoundException
import com.example.gateway.MessagePort
import com.example.gateway.UserNotFoundException
import com.example.gateway.UserPort
import com.example.valueobject.CreatedComment
import com.example.valueobject.CreatedMessage
import mu.KotlinLogging

class MessageUsecaseImpl(private val messagePort: MessagePort, private val userPort: UserPort) : MessageUsecase {
    val logger = KotlinLogging.logger {}
    override fun get(by: By, order: Order): Messages {
        return messagePort.getMessages().sorted(by, order).sortedComments(by, order)
    }

    override fun get(id: MessageId): Message = messagePort.getMessage(id)

    override fun getByUerId(userId: UserId, by: By, order: Order): Messages {
        return messagePort.getMessages(userId).sorted(by, order).sortedComments(by, order)
    }

    override fun create(message: CreatedMessage): MessageId {
        logger.debug { "create message" }
        if (userPort.isNotFound(message.userId)) throw UserNotFoundException(message.userId)
        return messagePort.createMessage(message)
    }

    override fun updated(id: MessageId, message: CreatedMessage) {
        if (userPort.isNotFound(message.userId)) throw UserNotFoundException(message.userId)
        messagePort.updateMessage(id, message)
    }

    override fun getComments(id: MessageId, by: By, order: Order): Comments =
        messagePort.getComments(id).sorted(by, order)

    override fun createComment(comment: CreatedComment): CommentId {
        if (userPort.isNotFound(comment.userId)) throw UserNotFoundException(comment.userId)
        return messagePort.createComment(comment)
    }

    override fun updateComment(id: CommentId, comment: CreatedComment) {
        if (userPort.isNotFound(comment.userId)) throw UserNotFoundException(comment.userId)
        if (messagePort.isNotFound(comment.messageId)) throw MessageNotFoundException(comment.messageId)
        messagePort.updateComment(id, comment)
    }
}