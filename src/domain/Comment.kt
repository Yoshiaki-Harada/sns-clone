package com.example.domain

import java.time.ZonedDateTime
import java.util.*

class Comment(
    val id: CommentId,
    val commentText: CommentText,
    val userId: UserId,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime
){
}

data class CommentText(val value: String) {

}

data class CommentId(val value: UUID) {

}
