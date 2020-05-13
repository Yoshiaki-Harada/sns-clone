package driver.entity

import io.requery.Entity
import io.requery.Key
import io.requery.Persistable
import io.requery.Table
import java.util.*

@Entity
@Table(name = "tags")
data class TagEntity(
    @get:Key
    var id: UUID,
    var name: String
): Persistable
