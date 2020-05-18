package com.example.domain

import java.util.*

class Tag(val id : TagId, val name: TagName)

class TagName(val value: String)

class TagId(val value: UUID)
