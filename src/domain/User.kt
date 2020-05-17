package com.example.domain

import java.util.*

class User(
    val id: UserId ,
    val name: UserName,
    val mail: Mail
) {
}

data class Mail(val value: String)

class UserName(val value: String)

class UserId(val value: UUID)
