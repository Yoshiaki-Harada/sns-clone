package com.example.gateway

import com.example.domain.*
import com.example.driver.dao.UserDao
import com.example.driver.entity.UserEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

class UserNotFoundException(id: UserId) : Throwable("user: ${id.value} not found")

class UserRepository(
    private val database: Database
) :
    UserPort {
    override fun find() =
        UserDao.find()
            .map { User(id = UserId(it.id.value), name = UserName(it.name), mail = Mail(it.mail)) }
            .let { Users(it) }

    override fun findBy(id: UserId): User {
        val user =
            UserDao.findByUserId(id.value) ?: throw UserNotFoundException(id)
        return User(UserId(user.id.value), UserName(user.name), Mail(user.mail))
    }

    override fun isFound(id: UserId): Boolean = kotlin.runCatching {
        findBy(id)
    }.isSuccess

    override fun isNotFound(id: UserId) = !isFound(id)

    override fun create(user: User): UserId {
        return transaction {
            UserDao.create(UserEntity(user.id.value, user.name.value, user.mail.value))
        }.let { UserId(it) }
    }


    override fun update(user: User) {
        if (!isFound(user.id)) throw UserNotFoundException(user.id)
        transaction {
            UserDao.update(UserEntity(user.id.value, user.name.value, user.mail.value))
        }
    }
}