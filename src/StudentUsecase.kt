package com.example

interface StudentUsecase {
    fun getStudents(): List<Student>
    fun createStudent(student: Student)
    fun updateStudent(student: Student)
    fun deleteStudent(id: Int)
}