package com.example.gateway

import com.example.domain.*
import com.example.driver.dao.UserDao
import com.example.driver.entity.UserEntity
import com.example.valueobject.CreatedUser
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

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

    override fun create(user: CreatedUser): UserId {
        return transaction {
            UserDao.create(UserEntity(UUID.randomUUID(), user.name.value, user.mail.value))
        }.let { UserId(it) }
    }


    override fun update(userId: UserId, user: CreatedUser) {
        if (!isFound(userId)) throw UserNotFoundException(userId)
        transaction {
            UserDao.update(UserEntity(userId.value, user.name.value, user.mail.value))
        }
    }
}