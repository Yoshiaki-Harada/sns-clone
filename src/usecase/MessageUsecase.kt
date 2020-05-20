package com.example.usecase

import com.example.domain.*
import com.example.valueobject.CreatedComment
import com.example.valueobject.CreatedMessage

interface MessageUsecase {
    fun get(by: By = By.CREATED_AT, order: Order = Order.ASC): Messages
    fun get(id: MessageId):Message
    fun getByUerId(userId: UserId, by: By = By.CREATED_AT, order: Order = Order.DESC): Messages
    fun create(message: CreatedMessage): MessageId
    fun updated(id: MessageId, message: CreatedMessage)
    fun getComments(id: MessageId, by: By = By.CREATED_AT, order: Order = Order.DESC):Comments
    fun createComment(comment: CreatedComment): CommentId
    fun updateComment(id: CommentId, comment: CreatedComment)
}