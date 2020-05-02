package com.example

import io.requery.Entity
import io.requery.Key
import io.requery.Persistable
import io.requery.Table

@Entity
@Table(name = "students")
interface StudentEntity : Persistable {
    @get:Key
    var id: Int
    var name: String
}

