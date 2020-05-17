package com.example.gateway

import com.example.domain.*

interface MessagePort {
    fun getMessages(): Messages
    fun getMessages(id: MessageId): Message
    fun getMessages(userId: UserId): Messages
//    fun createMessage(messages: Messages)
//    fun updateMessage(messageId: MessageId, text: MessageText)
}