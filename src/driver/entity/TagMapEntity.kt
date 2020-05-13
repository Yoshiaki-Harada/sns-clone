package driver.entity

import io.requery.*
import java.util.*

@Entity
@Table(name = "tag_map")
data class TagMapEntity(
    @get:Key
    var id: UUID,
    @get:ForeignKey(references = TagEntity::class)
    @get:Column(name = "tag_id")
    var tagId: UUID,
    @get:Column(name = "message_id")
    @get:ForeignKey(references = MessageEntity::class)
    var messageId: UUID
): Persistable
