package com.example.driver.dao

import com.example.domain.Comment
import com.example.domain.CommentId
import com.example.domain.CommentText
import com.example.domain.UserId
import com.example.driver.entity.CommentEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object Comments : UUIDTable() {
    val userId = reference("user_id", Users)
    val text = varchar("text", 100)
    val messageId = reference("message_id", Messages)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}

class CommentDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CommentDao>(Comments) {

        fun find() = all().toList()

        fun findByCommentId(id: UUID) = CommentDao.findById(id)

        fun findByUserId(userId: UUID) =
            find { Comments.userId eq userId }.toList()

        fun findByMessageId(messageId: UUID) =
            find { Comments.messageId eq messageId }.toList()

        fun findByMessages(messages: List<MessageDao>): List<List<CommentDao>> {
            return messages.map { findByMessageId(it.id.value) }
        }

        fun create(comment: CommentEntity): UUID {
            return CommentDao.new(comment.id) {
                messageId = EntityID(comment.messageId, Messages)
                userId = EntityID(comment.userId, Users)
                text = comment.text
            }.id.value
        }

        fun update(comment: CommentEntity) {
            val comment = findByCommentId(comment.id)
            comment?.let {
                it.messageId = comment.messageId
                it.text = comment.text
                it.userId = comment.userId
                it.updatedAt = LocalDateTime.now()
            }
        }
    }

    var userId by Comments.userId
    var messageId by Comments.messageId
    var text by Comments.text
    var createdAt by Comments.createdAt
    var updatedAt by Comments.updatedAt
}
