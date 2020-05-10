package com.example

import io.requery.Persistable
import io.requery.PersistenceException
import io.requery.kotlin.EntityStore
import io.requery.sql.KotlinEntityDataStore

typealias Store = EntityStore<Persistable, Any>

class NotFoundStudentException(id: Int) : Throwable("student id: $id is not found")
class PersistenceStudentException(id: Int) : Throwable("student id: $id is already persist")

class StudentRepository(private val dataStore: KotlinEntityDataStore<Persistable>, private val dao: StudentDao) :
    StudentPort {
    override fun findAll(): List<Student> = dao.findAll(this.dataStore)

    override fun findById(id: Int): Student = dao.findById(id, dataStore) ?: throw NotFoundStudentException(id)

    override fun create(student: Student) {
        kotlin.runCatching {
            dataStore.withTransaction {
                dao.create(student, this)
            }
        }.onFailure {
            when (it) {
                // requeryのcreateは既にPRIMARY KEYが存在するものを作成するとPersistenceExceptionを投げる
                is PersistenceException -> throw PersistenceStudentException(student.id)
                else -> {
                    throw it
                }
            }
        }
    }

    override fun update(student: Student) {
        this.findById(student.id)
        dataStore.withTransaction {
            dao.update(student, this)
        }
    }

    override fun delete(id: Int) {
        val count = dataStore.withTransaction {
            dao.delete(id, this)
        }
        if (count == 0) throw NotFoundStudentException(id)
    }
}