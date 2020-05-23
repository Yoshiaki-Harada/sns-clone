package com.example.driver.entity

import com.example.domain.*
import com.example.zoneId
import java.util.*

data class MessageEntity(
    val id: UUID,
    val userId: UUID,
    val text: String
)