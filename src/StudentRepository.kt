package com.example

import io.requery.Persistable
import io.requery.kotlin.EntityStore
import io.requery.sql.KotlinEntityDataStore
typealias Store = EntityStore<Persistable, Any>

class StudentRepository(private val dataStore: KotlinEntityDataStore<Persistable>, private val dao: StudentDao) :
    StudentPort {
    override fun findAll(): List<Student> = dao.findAll(this.dataStore)

    override fun create(student: Student) {
        dao.create(student, dataStore)
    }

    override fun update(student: Student) {
        dao.update(student, dataStore)
    }

    override fun delete(id: Int) {
        return dao.delete(id, dataStore)
    }
}