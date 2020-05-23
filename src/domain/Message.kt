package com.example.domain

import java.time.ZonedDateTime
import java.util.*

class Message(
    val id: MessageId,
    val userId: UserId,
    val text: MessageText,
    val tags: Tags,
    val comments: Comments,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime
)

class MessageText(val value: String)

class MessageId(val value: UUID)
