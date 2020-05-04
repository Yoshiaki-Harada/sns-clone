package com.example

interface StudentPort {
    fun findAll(): List<Student>
    fun findById(id: Int): Student
    fun create(student: Student)
    fun update(student: Student)
    fun delete(id: Int)
}