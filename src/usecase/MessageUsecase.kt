package com.example.usecase

import com.example.domain.*
import com.example.valueobject.CreatedMessage

interface MessageUsecase {
    fun get(by: By = By.CREATED_AT, order: Order = Order.ASC): Messages
    fun getByUerId(userId: UserId, by: By = By.CREATED_AT, order: Order = Order.ASC): Messages
    fun create(message: CreatedMessage): MessageId
    fun updated(id: MessageId, message: CreatedMessage)
}