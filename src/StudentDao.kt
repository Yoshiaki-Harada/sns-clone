package com.example

import io.requery.kotlin.EntityStore
import io.requery.Persistable
import io.requery.kotlin.eq


class StudentDao :Repository{

    override fun findAll(dataStore: EntityStore<Persistable, Any>): List<Student> = dataStore.select(Student::class).get().toList()

    override fun create(student: Student, dataStore: Store) {
        dataStore.insert(student)
    }

    override fun update(student: Student, dataStore: Store) {
        dataStore.update(student)
    }

    override fun delete(id: Int, dataStore: Store): Int =
        dataStore.delete(Student::class).where(Student::id eq id).get().value()
}