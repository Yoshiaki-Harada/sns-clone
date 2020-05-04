package com.example

import io.requery.Persistable
import io.requery.kotlin.EntityStore
import io.requery.sql.KotlinEntityDataStore
typealias Store = EntityStore<Persistable, Any>

class StudentRepository(private val dataStore: KotlinEntityDataStore<Persistable>, private val dao: StudentDao) :
    StudentPort {
    override fun findAll(): List<Student> = dao.findAll(this.dataStore)

    override fun findById(id: Int): Student = dao.findById(id, dataStore)!!

    override fun create(student: Student) {
        dataStore.withTransaction {
            dao.create(student, this)
        }
    }

    override fun update(student: Student) {
        dataStore.withTransaction {
            dao.update(student, this)
        }
    }

    override fun delete(id: Int) {
        dataStore.withTransaction {
             dao.delete(id, this)
        }
    }
}