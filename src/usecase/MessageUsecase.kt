package com.example.usecase

import com.example.domain.By
import com.example.domain.Messages
import com.example.domain.Order
import com.example.domain.UserId

interface MessageUsecase {
    fun get(by: By = By.CREATED_AT, order: Order = Order.ASC):Messages
    fun getByUerId(userId: UserId, by: By = By.CREATED_AT, order: Order = Order.ASC): Messages
}