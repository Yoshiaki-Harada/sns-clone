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
import org.kodein.di.generic.instance

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

data class JsonResponse(val message: String)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val usecase by Injector.kodein.instance<StudentUsecase>()
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    routing {
        get("/students") {
            call.respond(usecase.getStudents())
        }

        post("/students") {
            val inputJson = call.receive<Student>()
            usecase.createStudent(inputJson)
            call.respond(JsonResponse("id ${inputJson.id} is created"))
        }

        put("students/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val name = call.request.queryParameters["name"]!!
            val students = usecase.getStudents()

            if (students.indexOfFirst { it.id == id } < 0) {
                call.respond(HttpStatusCode.NotFound, JsonResponse("id $id is not found"))
                return@put
            }
            usecase.updateStudent(Student(id, name))
            call.respond(JsonResponse("id $id is updated"))
        }

        delete("students/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val students = usecase.getStudents()

            if (students.indexOfFirst { it.id == id } < 0) {
                call.respond(HttpStatusCode.NotFound, JsonResponse("id $id is not found"))
                return@delete
            }

            usecase.deleteStudent(id)
            call.respond(JsonResponse("id $id is deleted"))
        }
    }
}

