package com.example.driver.dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.util.*


object TagMap : UUIDTable(name = "tag_map") {
    val tagId = reference("tag_id", Tags)
    val messageId = reference("message_id", Messages)
}

class TagMapDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TagMapDao>(TagMap) {

        fun find() = transaction(
            transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
            repetitionAttempts = 2
        ) { all().toList() }

        fun findByMessageId(messageId: UUID) = transaction(
            transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
            repetitionAttempts = 2
        ) {
            TagMapDao.find {
                TagMap.messageId eq messageId
            }.toList()
        }

        fun create(id: UUID, tagId: UUID, messageId: UUID) = TagMapDao.new(id) {
            this.tagId = EntityID(tagId, Tags)
            this.messageId = EntityID(messageId, Messages)
        }

        fun deleteByMessageId(messageId: UUID) = findByMessageId(messageId).forEach { it.delete() }

        fun createIfNotExistMap(tagId: UUID, messageId: UUID) {
            if (TagMapDao.find { (TagMap.messageId eq messageId) and (TagMap.tagId eq tagId) }.count() == 0L) {
                create(UUID.randomUUID(), tagId, messageId)
            }
        }
    }

    var tagId by TagMap.tagId
    var messageId by TagMap.messageId
}
