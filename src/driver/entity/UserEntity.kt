package driver.entity

import io.requery.*
import java.util.*


@Entity
@Table(name = "users")
data class UsersEntity(
    @get:Key
    var id: UUID,
    var mail: String,
    var name: String,
    @get:Column(name = "created_at")
    var createdAt: java.sql.Timestamp,
    @get:Column(name = "updated_at")
    var updatedAt: java.sql.Timestamp
): Persistable

