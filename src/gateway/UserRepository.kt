package com.example.gateway

import com.example.domain.*
import com.example.driver.dao.CommentDao
import com.example.driver.dao.MessageDao
import com.example.driver.dao.UserDao
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.time.ZoneId

class UserNotFoundException(id: UserId) : Throwable("user: ${id.value} not found")

class UserRepository(
    val userDao: UserDao,
    val messageDao: MessageDao,
    val commentDao: CommentDao,
    val dataStore: Database
) :
    UserPort {
    private val zoneId = ZoneId.of("UTC")
    override fun findBy(id: UserId): User {
        val user = transaction(
            transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED,
            repetitionAttempts = 2,
            db = dataStore
        ) {
            userDao.findById(id.value)
        } ?: throw UserNotFoundException(id)
        val messages = transaction {
            messageDao.findByUserId(id.value)
        }.map {
            Message(
                MessageId(it.id.value),
                UserId(it.userId.value),
                MessageText(it.text),
                Tags(emptyList()),
                it.createdAt.atZone(zoneId),
                it.updatedAt.atZone(zoneId)
            )
        }.let {
            Messages(it)
        }
        val comments = transaction {
            commentDao.findByUserId(id.value)
        }.map {
            Comment(
                CommentId(it.id.value), CommentText(it.text),
                it.createdAt.atZone(zoneId),
                it.updatedAt.atZone(zoneId)
            )
        }.let {
            Comments(it)
        }

        return User(UserId(user.id.value), UserName(user.name), Mail(user.mail), messages, comments)
    }
}