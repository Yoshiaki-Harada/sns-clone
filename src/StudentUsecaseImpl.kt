package com.example

class StudentUsecaseImpl(val port: StudentPort) :StudentUsecase{
    override fun getStudents(): List<Student> = port.findAll()

    override fun createStudent(student: Student) = port.create(student)

    override fun updateStudent(student: Student) = port.update(student)

    override fun deleteStudent(id: Int) = port.delete(id)
}