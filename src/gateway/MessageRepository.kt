package com.example.gateway

import com.example.domain.*
import com.example.driver.dao.CommentDao
import com.example.driver.dao.MessageDao
import com.example.driver.entity.CommentEntity
import com.example.driver.entity.MessageEntity
import com.example.valueobject.CreatedComment
import com.example.valueobject.CreatedMessage
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class MessageNotFoundException(id: MessageId) : Throwable("user: ${id.value} not found")

class MessageRepository(
    val database: Database
) : MessagePort {

    override fun getMessages(): Messages = MessageDao.find().map {
        val comment = CommentDao.findByMessageId(it.id.value).map(
            CommentDao::toDomain
        )
        it.toDomain(Comments(comment))
    }.let {
        Messages(it)
    }

    override fun getMessage(id: MessageId): Message {
        return MessageDao.findByMessageId(id.value)?.let {
            val comment = CommentDao.findByMessageId(it.id.value).map(
                CommentDao::toDomain
            )
            it.toDomain(Comments(comment))
        } ?: throw MessageNotFoundException(id)
    }

    override fun getMessages(userId: UserId): Messages {
        val messages = MessageDao.findByUserId(userId.value)
        return messages.map {
            val comment = CommentDao.findByMessageId(it.id.value).map(
                CommentDao::toDomain
            )
            it.toDomain(Comments(comment))
        }.let {
            Messages(it)
        }
    }

    override fun isFound(id: MessageId): Boolean = kotlin.runCatching {
        getMessage(id)
    }.isSuccess

    override fun isNotFound(id: MessageId) = !isFound(id)

    override fun createMessage(message: CreatedMessage): MessageId = transaction {
        MessageDao.create(
            MessageEntity(
                UUID.randomUUID(),
                message.userId.value,
                message.text.value
            )
        ).let {
            MessageId(it)
        }
    }

    override fun updateMessage(messageId: MessageId, message: CreatedMessage) {
        transaction {
            MessageDao.update(
                MessageEntity(
                    id = messageId.value,
                    userId = message.userId.value,
                    text = message.text.value
                )
            )
        }
    }

    override fun getComments(messageId: MessageId): Comments =
        CommentDao
            .findByMessageId(messageId.value)
            .map(CommentDao::toDomain).let {
                Comments(it)
            }

    override fun createComment(comment: CreatedComment) = transaction {
        CommentDao.create(
            CommentEntity(
                id = UUID.randomUUID(),
                messageId = comment.messageId.value,
                userId = comment.userId.value,
                text = comment.text.value
            )
        ).let {
            CommentId(it)
        }
    }


    override fun updateComment(commentId: CommentId, comment: CreatedComment) {
        transaction {
            CommentDao.update(
                CommentEntity(
                    id = commentId.value,
                    messageId = comment.messageId.value,
                    userId = comment.userId.value,
                    text = comment.text.value
                )
            )
        }
    }
}
