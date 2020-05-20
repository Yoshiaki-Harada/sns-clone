package com.example.gateway

import com.example.domain.*
import com.example.valueobject.CreatedComment
import com.example.valueobject.CreatedMessage

interface MessagePort {
    fun getMessages(): Messages
    fun getMessage(id: MessageId): Message
    fun getMessages(userId: UserId): Messages
    fun isFound(id: MessageId): Boolean
    fun isNotFound(id: MessageId): Boolean
    fun createMessage(message: CreatedMessage): MessageId
    fun updateMessage(messageId: MessageId, message: CreatedMessage)
    fun getComments(messageId: MessageId): Comments
    fun createComment(comment: CreatedComment): CommentId
    fun updateComment(messageId: CommentId, comment: CreatedComment)
}