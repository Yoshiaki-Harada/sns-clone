package com.example

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val students = listOf(Student(1, "Taro"), Student(2, "Hanako"), Student(3, "Yuta"))

data class Student(val id: Int, val name: String)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {
        get("/hello") {
            call.respond("Hello World")
        }
    }
}

