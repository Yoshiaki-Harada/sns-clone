package com.example

import com.mysql.cj.jdbc.MysqlDataSource
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.requery.Persistable
import io.requery.sql.KotlinConfiguration
import io.requery.sql.KotlinEntityDataStore

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val students = mutableListOf(Student(1, "Taro"), Student(2, "Hanako"), Student(3, "Yuta"))
val dataSource = MysqlDataSource().apply {
    serverName = "127.0.0.1"
    port = 3306
    user = "root"
    password = "mysql"
    databaseName = "school"
}
val dataStore = KotlinEntityDataStore<Persistable>(KotlinConfiguration(dataSource = dataSource, model = Models.DEFAULT))

data class JsonResponse(val message: String)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val dao = StudentDao()
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    routing {
        get("/students") {
            call.respond(dao.findAll(dataStore))
        }

        post("/students") {
            val inputJson = call.receive<Student>()
            students.add(inputJson)
            dataStore.withTransaction {
                dao.create(inputJson, this)
            }
            call.respond(JsonResponse("id ${inputJson.id} is created"))
        }

        put("students/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val name = call.request.queryParameters["name"]!!
            val students = dao.findAll(dataStore)

            if (students.indexOfFirst { it.id == id } < 0) {
                call.respond(HttpStatusCode.NotFound, JsonResponse("id $id is not found"))
                return@put
            }

            dataStore.withTransaction {
                dao.update(Student(id, name), this)
            }
            call.respond(JsonResponse("id $id is updated"))
        }

        delete("students/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val students = dao.findAll(dataStore)

            if (students.indexOfFirst { it.id == id } < 0) {
                call.respond(HttpStatusCode.NotFound, JsonResponse("id $id is not found"))
                return@delete
            }

            dataStore.withTransaction {
                dao.delete(id, this)
            }
            call.respond(JsonResponse("id $id is deleted"))
        }
    }
}

