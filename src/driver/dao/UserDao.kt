package com.example.driver.dao

import driver.entity.UserEntity
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.util.*

class UserDao {
    fun find() = transaction(
        transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
        repetitionAttempts = 2
    ) { UserEntity.all().toList() }

    fun findById(id: UUID): UserEntity? = transaction(
        transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
        repetitionAttempts = 2
    ) {
        UserEntity.findById(id)
    }

    fun create(user: CreateUser): UUID {
        return UserEntity.new(user.id) {
            name = user.name
            mail = user.mail
        }.id.value
    }

    fun update(id: UUID, updateUser: UpdateUser) {
        val user = findById(id)
        println("name ${updateUser.name}")
        user?.let {
            it.name = updateUser.name
            it.mail = updateUser.mail
        }
    }
}

data class CreateUser(val id: UUID, val name: String, val mail: String)
data class UpdateUser(val name: String, val mail: String)
