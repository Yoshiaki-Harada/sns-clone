package com.example

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

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

        put("students/{id}") {
            val id = call.parameters["id"]!!.toInt() //パスパラメータを受け取る
            val name = call.request.queryParameters["name"]!! //クエリパラメータを受け取る
            // 指定したidが存在しなければ404 not foundを返す
            if (students.find { it.id == id } == null) {
                call.respond(HttpStatusCode.NotFound, "$id is not found")
                return@put
            }
            // 古いデータを削除して新しく作成する
            students.removeIf { it.id == id }
            students.add(Student(id, name))
            call.respond("$id is updated")
        }

        delete("students/{id}") {
            val id = call.parameters["id"]!!.toInt() //パスパラメータを受け取る
            if (students.removeIf { it.id == id }) {
                call.respond("$id is deleted")
                return@delete
            }
            call.respond(HttpStatusCode.NotFound, "$id is not found")
        }
    }
}

