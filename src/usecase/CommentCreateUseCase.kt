package com.example.usecase

import com.example.domain.CommentId
import com.example.valueobject.CreatedComment

interface CommentCreateUseCase {
    fun execute(comment: CreatedComment): CommentId
}