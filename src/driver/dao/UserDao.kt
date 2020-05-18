package com.example.driver.dao

import com.example.driver.entity.UserEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.util.*


object Users : UUIDTable() {
    val name = varchar("name", 100)
    val mail = varchar("mail", 100)
}

class UserDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserDao>(Users) {
        fun find() = transaction(
            transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
            repetitionAttempts = 2
        ) { all().toList() }


        fun findByUserId(id: UUID): UserDao? = transaction(
            transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
            repetitionAttempts = 2
        ) {
            UserDao.findById(id)
        }

        fun create(user: UserEntity): UUID {
            return UserDao.new(user.id) {
                name = user.name
                mail = user.mail
            }.id.value
        }

        fun update(updateUser: UserEntity) {
            val user = findById(updateUser.id)
            println("name ${updateUser.name}")
            user?.let {
                it.name = updateUser.name
                it.mail = updateUser.mail
            }
        }
    }

    var name by Users.name
    var mail by Users.mail


}