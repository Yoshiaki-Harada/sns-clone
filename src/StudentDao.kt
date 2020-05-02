package com.example

import io.requery.kotlin.EntityStore
import io.requery.Persistable
import io.requery.kotlin.eq

typealias Store = EntityStore<Persistable, Any>

class StudentDao {

    fun findAll(dataStore: EntityStore<Persistable, Any>): List<Student> = dataStore.select(Student::class).get().toList()

    fun create(student: Student, dataStore: EntityStore<Persistable, Any>) {
        dataStore.insert(student)
    }

    fun update(student: Student, dataStore: EntityStore<Persistable, Any>) {
        dataStore.update(student)
    }

    fun delete(id: Int, dataStore: EntityStore<Persistable, Any>): Int =
        dataStore.delete(Student::class).where(Student::id eq id).get().value()
}