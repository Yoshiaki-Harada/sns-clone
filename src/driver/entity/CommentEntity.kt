package driver.entity

import com.example.domain.Comment
import com.example.domain.CommentId
import com.example.domain.CommentText
import com.example.domain.UserId
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.ZoneId
import java.util.*

object Comments : UUIDTable() {
    val userId = reference("user_id", Users)
    val text = varchar("text", 100)
    val messageId = reference("message_id", Messages)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}

class CommentEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CommentEntity>(Comments)

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
