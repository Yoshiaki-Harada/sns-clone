package com.example.gateway

import com.example.domain.*
import com.example.driver.dao.CommentDao
import com.example.driver.dao.MessageDao
import driver.entity.CommentEntity
import org.jetbrains.exposed.sql.Database

class MessageNotFoundException(id: MessageId) : Throwable("user: ${id.value} not found")

class MessageRepository(private val messageDao: MessageDao, private val commentDao: CommentDao,val database: Database) : MessagePort {

    override fun getMessages(): Messages = messageDao.find().map {
        val comment = commentDao.findByMessageId(it.id.value).map(CommentEntity::toDomain)
        it.toDomain(Comments(comment))
    }.let {
        Messages(it)
    }

    override fun getMessages(id: MessageId): Message {
        return messageDao.findById(id.value)?.let {
            val comment = commentDao.findByMessageId(it.id.value).map(CommentEntity::toDomain)
            it.toDomain(Comments(comment))
        } ?: throw MessageNotFoundException(id)
    }

    override fun getMessages(userId: UserId): Messages {
        val messages = messageDao.findByUserId(userId.value)
        return messages.map {
            val comment = commentDao.findByMessageId(it.id.value).map(CommentEntity::toDomain)
            it.toDomain(Comments(comment))
        }.let {
            Messages(it)
        }
    }
}
