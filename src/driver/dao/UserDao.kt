package com.example.driver.dao

import driver.entity.UserEntity
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.util.*

class UserDao {
    fun findById(id: UUID): UserEntity? = transaction(
        transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
        repetitionAttempts = 2
    ) {
        UserEntity.findById(id)
    }
}