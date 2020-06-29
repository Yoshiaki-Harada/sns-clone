package com.example.usecase

import com.example.domain.CommentId
import com.example.valueobject.CreatedComment

interface CommentUpdateUseCase {
    fun execute(id: CommentId, comment: CreatedComment)
}