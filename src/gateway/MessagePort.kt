package com.example.gateway

import com.example.domain.Message
import com.example.domain.MessageId
import com.example.domain.Messages
import com.example.domain.UserId

interface MessagePort {
    fun getMessages(): Messages
    fun getMessages(id: MessageId): Message
    fun getMessages(userId: UserId): Messages
}