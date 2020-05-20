package com.example.handler

import com.example.ValidationError
import com.example.domain.By
import com.example.domain.Order
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

val f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

fun ZonedDateTime.toStr() = this.format(f)

fun getId(id: String): UUID = runCatching {
    UUID.fromString(id)
}.getOrElse {
    throw ValidationError(it.message ?: "Unkown error")
}

class OrderBy(val order: Order, val by: By) {
    companion object {
        fun of(order: String, by: String): OrderBy {
            val order = kotlin.runCatching {
                Order.getType(order)
            }.onFailure {
                throw ValidationError("order must be in ${Order.getValues()}, but ${order}")
            }.getOrThrow()
            val by = kotlin.runCatching {
                By.getType(by)
            }.onFailure {
                throw ValidationError("by must be in ${By.getValues()}, but ${by}")
            }.getOrThrow()
            return OrderBy(order, by)
        }
    }
}
