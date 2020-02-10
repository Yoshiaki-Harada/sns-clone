package com.example

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val students = mutableListOf(Student(1, "Taro"), Student(2, "Hanako"), Student(3, "Yuta"))

data class Student(val id: Int, val name: String)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    routing {
        get("/students") {
            call.respond(students)
        }

        post("/students") {
            val inputJson = call.receive<Student>()
            students.add(inputJson)
            call.respond(inputJson.id)
        }
    }
}

