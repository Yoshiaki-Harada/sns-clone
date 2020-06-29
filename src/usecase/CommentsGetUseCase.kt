package com.example.usecase

import com.example.domain.By
import com.example.domain.Comments
import com.example.domain.MessageId
import com.example.domain.Order

interface CommentsGetUseCase {
    fun execute(id: MessageId, by: By, order: Order): Comments
}