package com.example.driver.dao

import driver.entity.MessageEntity
import driver.entity.Messages
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.util.*

class MessageDao {
    fun findById(id: UUID) = MessageEntity.findById(id)

    fun findByUserId(userId: UUID): List<MessageEntity> =
        transaction(
            transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
            repetitionAttempts = 2
        ) {
            MessageEntity.find { Messages.userId eq userId }.toList()
        }

}