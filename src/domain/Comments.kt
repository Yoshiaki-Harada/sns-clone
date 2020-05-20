package com.example.domain

class Comments(val list: List<Comment>) {
    fun sorted(by: By, order: Order): Comments = when (by) {
        By.UPDATED_AT -> when (order) {
            Order.ASC -> list.sortedBy { it.updatedAt }
            Order.DESC -> list.sortedByDescending { it.updatedAt }
        }
        By.CREATED_AT -> when (order) {
            Order.ASC -> list.sortedBy { it.createdAt }
            Order.DESC -> list.sortedByDescending { it.createdAt }
        }
    }.let { Comments(it) }
}
