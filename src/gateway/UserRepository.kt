package com.example.gateway

import com.example.domain.Mail
import com.example.domain.User
import com.example.domain.UserId
import com.example.domain.UserName
import com.example.driver.dao.UserDao
import org.jetbrains.exposed.sql.Database
import java.time.ZoneId

class UserNotFoundException(id: UserId) : Throwable("user: ${id.value} not found")

class UserRepository(
   private val userDao: UserDao,
    private val database: Database
) :
    UserPort {
    private val zoneId = ZoneId.of("UTC")
    override fun findBy(id: UserId): User {
        val user =
            userDao.findById(id.value) ?: throw UserNotFoundException(id)
        return User(UserId(user.id.value), UserName(user.name), Mail(user.mail))
    }
}