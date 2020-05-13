package driver.entity

import io.requery.*
import java.util.*


@Entity
@Table(name = "messages")
data class MessageEntity (
    @get:Key
    var id: UUID,
    @get:ForeignKey(references = UsersEntity::class)
    @get:Column(name = "user_id")
    var userId: UUID,
    var text: String,
    var createdAt: java.sql.Timestamp,
    var updatedAt: java.sql.Timestamp
): Persistable


