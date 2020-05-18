package com.example.gateway

import com.example.domain.*
import com.example.driver.dao.CommentDao
import com.example.driver.dao.MessageDao
import com.example.driver.entity.MessageEntity
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

    override fun getMessages(id: MessageId): Message {
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

    override fun createMessage(message: CreatedMessage): MessageId = transaction {
        MessageId(
            MessageDao.create(
                MessageEntity(
                    UUID.randomUUID(),
                    message.userId.value,
                    message.text.value
                )
            )
        )
    }

    override fun updateMessage(messageId: MessageId, message: CreatedMessage) {
        transaction {
            MessageDao.update(MessageEntity(messageId.value, message.userId.value, message.text.value))
        }
    }
}
