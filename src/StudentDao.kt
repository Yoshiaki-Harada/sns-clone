package com.example

import io.requery.Persistable
import io.requery.kotlin.EntityStore
import io.requery.kotlin.eq


class StudentDao {

    fun findAll(dataStore: EntityStore<Persistable, Any>): List<Student> =
        dataStore.select(Student::class).get().toList()

    fun create(student: Student, dataStore: Store) {
        dataStore.insert(student)
    }

    fun update(student: Student, dataStore: Store) {
        dataStore.update(student)
    }

    fun delete(id: Int, dataStore: Store) {
        dataStore.delete(Student::class).where(Student::id eq id).get().value()
    }
}