package com.example.domain

import java.time.ZonedDateTime

class MessageInfo(
    val id: MessageId,
    val text: MessageText,
    val tags: Tags,
    val comments: Comments,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime
) {
}