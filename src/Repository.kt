package com.example

import io.requery.Persistable
import io.requery.kotlin.EntityStore

typealias Store = EntityStore<Persistable, Any>

interface Repository {
    fun findAll(dataStore: EntityStore<Persistable, Any>): List<Student>
    fun create(student: Student, dataStore: Store)
    fun update(student: Student, dataStore: Store)
    fun delete(id: Int, dataStore: Store): Int
}