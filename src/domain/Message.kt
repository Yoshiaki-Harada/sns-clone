package com.example.domain

import java.util.*

class Message(
    val user: User,
    val messageInfo: MessageInfo
)

class MessageText(val value: String)

class MessageId(val value: UUID)
