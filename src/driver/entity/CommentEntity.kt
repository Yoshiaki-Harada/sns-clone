package com.example.driver.entity

import java.util.*

data class CommentEntity(val id: UUID, val messageId: UUID, val userId: UUID, val text: String)