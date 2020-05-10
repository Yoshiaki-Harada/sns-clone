package com.example

import io.requery.kotlin.eq


class StudentDao {

    fun findAll(dataStore: Store): List<Student> =
        dataStore.select(Student::class).get().toList()

    fun findById(id: Int, dataStore: Store): Student? {
        val cond = Student::id eq id
        return dataStore.select(Student::class).where(cond).get().firstOrNull()
    }

    fun create(student: Student, dataStore: Store) {
        dataStore.insert(student)
    }

    fun update(student: Student, dataStore: Store) {
        dataStore.update(student)
    }

    fun delete(id: Int, dataStore: Store): Int {
        val cond = Student::id eq id
        return dataStore.delete(Student::class).where(cond).get().value()
    }
}