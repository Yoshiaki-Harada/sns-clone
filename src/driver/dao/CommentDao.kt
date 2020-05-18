package com.example.driver.dao

import com.example.domain.Comment
import com.example.domain.CommentId
import com.example.domain.CommentText
import com.example.domain.UserId
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.time.ZoneId
import java.util.*

object Comments : UUIDTable() {
    val userId = reference("user_id", Users)
    val text = varchar("text", 100)
    val messageId = reference("message_id", Messages)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}

class CommentDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CommentDao>(Comments) {

        fun find() = transaction(
            transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
            repetitionAttempts = 2
        ) { all().toList() }

        fun findByCommentId(id: UUID) = transaction(
            transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
            repetitionAttempts = 2
        ) {
            CommentDao.findById(id)
        }

        fun findByUserId(userId: UUID) =
            transaction(
                transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
                repetitionAttempts = 2
            ) {
                find { Comments.userId eq userId }.toList()
            }

        fun findByMessageId(messageId: UUID) =
            transaction(
                transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
                repetitionAttempts = 2
            ) {
                find { Comments.messageId eq messageId }.toList()
            }

        fun findByMessages(messages: List<MessageDao>): List<List<CommentDao>> {
            return messages.map { findByMessageId(it.id.value) }
        }

    }

    var userId by Comments.userId
    var messageId by Comments.messageId
    var text by Comments.text
    var createdAt by Comments.createdAt
    var updatedAt by Comments.updatedAt

    private val zoneId = ZoneId.of("UTC")
    fun toDomain() = Comment(
        id = CommentId(id.value),
        commentText = CommentText(text),
        userId = UserId(userId.value),
        createdAt = createdAt.atZone(zoneId),
        updatedAt = updatedAt.atZone(zoneId)
    )
}
