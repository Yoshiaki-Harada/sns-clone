package com.example.gateway

import com.example.domain.Mail
import com.example.domain.User
import com.example.domain.UserId
import com.example.domain.UserName
import com.example.driver.dao.CreateUser
import com.example.driver.dao.UpdateUser
import com.example.driver.dao.UserDao
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

class UserNotFoundException(id: UserId) : Throwable("user: ${id.value} not found")

class UserRepository(
    private val userDao: UserDao,
    private val database: Database
) :
    UserPort {
    override fun findBy(id: UserId): User {
        val user =
            userDao.findById(id.value) ?: throw UserNotFoundException(id)
        return User(UserId(user.id.value), UserName(user.name), Mail(user.mail))
    }

    override fun exist(id: UserId): Boolean = kotlin.runCatching {
        findBy(id)
    }.isSuccess

    override fun create(user: User): UserId {
        return transaction {
            userDao.create(CreateUser(user.id.value, user.name.value, user.mail.value))
        }.let { UserId(it) }
    }


    override fun update(user: User) {
        if (!exist(user.id)) throw UserNotFoundException(user.id)
        transaction {
            userDao.update(user.id.value, UpdateUser(user.name.value, user.mail.value))
        }
    }
}