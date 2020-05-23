package com.example.driver.dao

import com.example.domain.Tag
import com.example.domain.TagId
import com.example.domain.TagName
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.select
import java.util.*

object Tags : UUIDTable() {
    val name = varchar("name", 100)
}

class TagDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TagDao>(Tags) {

        fun find() = all().toList()

        fun findByName(name: String) =
            TagDao.find { Tags.name eq name }.firstOrNull()

        fun create(id: UUID, tagName: String) = TagDao.new(id) {
            name = tagName
        }

        fun findOrCreate(name: String): UUID {
            return findByName(name)?.let {
                it.id.value
            } ?: let {
                val tagId = UUID.randomUUID()
                create(tagId, name).id.value
            }
        }

        fun findByMessageId(messageId: UUID) =
            Tags.join(TagMap, JoinType.LEFT, TagMap.tagId).slice(Tags.columns).select {
                TagMap.messageId eq messageId
            }
    }

    var name by Tags.name
}
