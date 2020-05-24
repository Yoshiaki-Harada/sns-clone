package com.example.gateway

import com.example.domain.*
import com.example.driver.dao.CommentDao
import com.example.driver.dao.MessageDao
import com.example.driver.dao.TagDao
import com.example.driver.dao.TagMapDao
import com.example.driver.entity.CommentEntity
import com.example.driver.entity.MessageEntity
import com.example.valueobject.CreatedComment
import com.example.valueobject.CreatedMessage
import com.example.zoneId
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.util.*

class MessageNotFoundException(id: MessageId) : Throwable("user: ${id.value} not found")

class MessageRepository(
    val database: Database
) : MessagePort {
    private val logger = KotlinLogging.logger {}

    override fun getMessages(): Messages = transaction(
        transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
        repetitionAttempts = 2
    ) {
        MessageDao.find().map {
            val comment = CommentDao.findByMessageId(it.id.value).map { c -> c.toDomain() }
            val tags = TagDao.findByMessageId(it.id.value).map { result ->
                val id = result[com.example.driver.dao.Tags.id].value
                val name = result[com.example.driver.dao.Tags.name]
                Tag(TagId(id), TagName(name))
            }
            it.toDomain(Comments(comment), Tags(tags))
        }.let {
            Messages(it)
        }
    }

    override fun getMessage(id: MessageId): Message {
        return transaction(
            transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
            repetitionAttempts = 2
        ) {
            MessageDao.findByMessageId(id.value)?.let {
                val comment = CommentDao.findByMessageId(it.id.value).map { c -> c.toDomain() }
                val tags = TagDao.findByMessageId(it.id.value).map { result ->
                    val id = result[com.example.driver.dao.Tags.id].value
                    val name = result[com.example.driver.dao.Tags.name]
                    Tag(TagId(id), TagName(name))
                }
                it.toDomain(Comments(comment), Tags(tags))
            } ?: throw MessageNotFoundException(id)
        }
    }

    override fun getMessages(userId: UserId): Messages = transaction(
        transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
        repetitionAttempts = 2
    ) {
        return@transaction MessageDao.findByUserId(userId.value).map {
            val comment = CommentDao.findByMessageId(it.id.value).map { c -> c.toDomain() }
            val tags = TagDao.findByMessageId(it.id.value).map { result ->
                val id = result[com.example.driver.dao.Tags.id].value
                val name = result[com.example.driver.dao.Tags.name]
                Tag(TagId(id), TagName(name))
            }
            it.toDomain(Comments(comment), Tags(tags))
        }.let {
            Messages(it)
        }
    }

    override fun isFound(id: MessageId): Boolean = kotlin.runCatching {
        getMessage(id)
    }.isSuccess

    override fun isNotFound(id: MessageId) = !isFound(id)

    override fun createMessage(message: CreatedMessage): MessageId {
        return transaction {

            val messageId = MessageDao.create(
                MessageEntity(
                    UUID.randomUUID(),
                    message.userId.value,
                    message.text.value
                )
            )
            message.tags.list.forEach {
                val tagId = TagDao.findOrCreate(it.name)
                TagMapDao.create(UUID.randomUUID(), tagId, messageId)
            }
            MessageId(messageId)
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

            message.tags.list.forEach {
                val tagId = TagDao.findOrCreate(it.name)
                TagMapDao.createIfNotExistMap(tagId, messageId.value)
            }
        }
    }

    override fun getComments(messageId: MessageId): Comments =
        CommentDao
            .findByMessageId(messageId.value)
            .map { it.toDomain() }
            .let {
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

    fun MessageDao.toDomain(comments: Comments, tags: Tags) =
        Message(
            id = MessageId(id.value),
            userId = UserId(userId.value),
            text = MessageText(text),
            tags = tags,
            comments = comments,
            createdAt = createdAt.atZone(zoneId),
            updatedAt = updatedAt.atZone(zoneId)
        )

    fun CommentDao.toDomain() = Comment(
        id = CommentId(id.value),
        commentText = CommentText(text),
        userId = UserId(userId.value),
        createdAt = createdAt.atZone(zoneId),
        updatedAt = updatedAt.atZone(zoneId)
    )
}
