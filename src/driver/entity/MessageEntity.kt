package driver.entity

import com.example.domain.*
import com.example.domain.Comments
import com.example.zoneId
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.util.*


object Messages : UUIDTable() {
    val userId = reference("user_id", Users)
    val text = varchar("text", 100)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}

class MessageEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MessageEntity>(Messages)

    var userId by Messages.userId
    var text by Messages.text
    var createdAt by Messages.createdAt
    var updatedAt by Messages.updatedAt

    fun toDomain(comments: Comments) =
        Message(
            id = MessageId(id.value),
            userId = UserId(userId.value),
            text = MessageText(text),
            tags = Tags(emptyList()),
            comments = comments,
            createdAt = createdAt.atZone(zoneId),
            updatedAt = updatedAt.atZone(zoneId)
        )
}
