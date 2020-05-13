package com.example.domain

class Message(val id :MessageId, val userId :UserId, val text: MessageText, val tags: Tags) {
}

class MessageText(val value: String)

class MessageId(val value: String)
