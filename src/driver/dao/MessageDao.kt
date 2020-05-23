package com.example.driver.dao

import com.example.domain.*
import com.example.domain.Comments
import com.example.domain.Tags
import com.example.driver.entity.MessageEntity
import com.example.zoneId
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime
import java.util.*


object Messages : UUIDTable() {
    val userId = reference("user_id", Users)
    val text = varchar("text", 100)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}

class MessageDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MessageDao>(Messages) {

        fun find() =
            MessageDao.all().toList()


        fun findByMessageId(id: UUID) = MessageDao.findById(id)

        fun findByUserId(userId: UUID): List<MessageDao> =
            MessageDao.find { Messages.userId eq userId }.toList()


        fun create(message: MessageEntity) = MessageDao.new(message.id) {
            text = message.text
            userId = EntityID(message.userId, Users)
        }.id.value


        fun update(message: MessageEntity) {
            val user = findByMessageId(message.id)
            user?.let {
                it.text = message.text
                it.userId = EntityID(message.userId, Users)
                it.updatedAt = LocalDateTime.now()
            }
        }
    }

    var userId by Messages.userId
    var text by Messages.text
    var createdAt by Messages.createdAt
    var updatedAt by Messages.updatedAt
}
