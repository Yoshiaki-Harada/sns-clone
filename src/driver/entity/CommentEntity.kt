package driver.entity

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.util.*

object Comments : UUIDTable() {
    val userId = reference("user_id", Users)
    val text = varchar("text", 100)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}

class CommentEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CommentEntity>(Comments)

    var userId by Comments.userId
    var text by Comments.text
    var createdAt by Comments.createdAt
    var updatedAt by Comments.updatedAt
}
