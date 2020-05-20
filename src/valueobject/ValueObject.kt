package com.example.valueobject

import com.example.domain.*

data class CreatedUser(val name: UserName, val mail: Mail)

data class CreatedMessage(val userId: UserId, val text: MessageText, val tags: CreatedTags)

data class CreatedTag(val name: String)

data class CreatedTags(val list: List<CreatedTag>)

data class CreatedComment(val messageId: MessageId, val userId: UserId, val text: CommentText)