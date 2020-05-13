package driver.entity

import io.requery.*
import org.kodein.di.generic.M
import org.kodein.di.softReference
import java.util.*

@Entity
@Table(name = "comments")
data class CommentEntity(
    @get:Key
    var id: UUID,
    @get:ForeignKey(references = UsersEntity::class)
    @get:Column(name = "user_id")
    var userId: UUID,
    @get:ForeignKey(references = MessageEntity::class)
    @get:Column(name = "message_id")
    var messageId: UUID,
    var text: String,
    var createdAt: java.sql.Timestamp,
    var updatedAt: java.sql.Timestamp
) : Persistable
