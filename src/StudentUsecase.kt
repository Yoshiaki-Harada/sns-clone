package com.example

interface StudentUsecase {
    fun getStudents(): List<Student>
    fun getStudentById(id: Int): Student
    fun createStudent(student: Student)
    fun updateStudent(student: Student)
    fun deleteStudent(id: Int)
}