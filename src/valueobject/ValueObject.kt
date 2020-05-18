package com.example.valueobject

import com.example.domain.MessageText
import com.example.domain.UserId

data class CreatedMessage(val userId: UserId, val text: MessageText, val tags: CreatedTags)

data class CreatedTag(val name: String)

data class CreatedTags(val list: List<CreatedTag>)