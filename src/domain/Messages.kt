package com.example.domain

enum class Order(val value: String) {
    ASC("asc"), DESC("desc");

    companion object {
        fun getType(value: String) = values().firstOrNull { it.value == value } ?: throw IllegalArgumentException()
        fun getValues() = values().map { it.value }.toList()
    }
}

enum class By(val value: String) {
    UPDATED_AT("updatedAt"), CREATED_AT("createdAt");

    companion object {
        fun getType(value: String) = values().firstOrNull { it.value == value } ?: throw IllegalArgumentException()
        fun getValues() = values().map { it.value }.toList()
    }
}

class Messages(val list: List<Message>) {
    fun sorted(by: By, order: Order): Messages = when (by) {
        By.UPDATED_AT -> when (order) {
            Order.ASC -> list.sortedBy { it.updatedAt }
            Order.DESC -> list.sortedByDescending { it.updatedAt }
        }
        By.CREATED_AT -> when (order) {
            Order.ASC -> list.sortedBy { it.createdAt }
            Order.DESC -> list.sortedBy { it.createdAt }
        }
    }.let { Messages(it) }

    fun sortedComments(by: By, order: Order) = list.map {
        Message(
            id = it.id,
            text = it.text,
            userId = it.userId,
            comments = it.comments.sorted(by, order),
            tags = it.tags,
            createdAt = it.createdAt,
            updatedAt = it.updatedAt
        )

    }.let {
        Messages(it)
    }
}