package com.example.driver.dao

import driver.entity.CommentEntity
import driver.entity.Comments
import driver.entity.MessageEntity
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.util.*

class CommentDao {
    fun find() = transaction(
        transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
        repetitionAttempts = 2
    ) { CommentEntity.all().toList() }

    fun findById(id: UUID) = transaction(
        transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
        repetitionAttempts = 2
    ) {
        CommentEntity.findById(id)
    }

    fun findByUserId(userId: UUID) =
        transaction(
            transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
            repetitionAttempts = 2
        ) {
            CommentEntity.find { Comments.userId eq userId }.toList()
        }

    fun findByMessageId(messageId: UUID) =
        transaction(
            transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
            repetitionAttempts = 2
        ) {
            CommentEntity.find { Comments.messageId eq messageId }.toList()
        }

    fun findByMessages(messages: List<MessageEntity>): List<List<CommentEntity>> {
        return messages.map { findByMessageId(it.id.value) }
    }
}