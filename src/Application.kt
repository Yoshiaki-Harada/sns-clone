package com.example

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import org.kodein.di.generic.instance

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

data class JsonResponse(val message: String)
data class JsonErrorResponse(val error: String)

class ValidationError(override val message: String) : Throwable(message)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val usecase by Injector.kodein.instance<StudentUsecase>()
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(StatusPages) {
        exception<NotFoundStudentException> { cause ->
            val errorMessage: String = cause.message ?: "Unknown error"
            call.respond(HttpStatusCode.NotFound, JsonErrorResponse(errorMessage))
        }
        exception<PersistenceStudentException> { cause ->
            val errorMessage: String = cause.message ?: "Unknown error"
            call.respond(HttpStatusCode.Conflict, JsonErrorResponse(errorMessage))
        }
        exception<ValidationError> { cause ->
            val errorMessage: String = cause.message
            call.respond(HttpStatusCode.BadRequest, JsonErrorResponse(errorMessage))
        }
        exception<Throwable> { cause ->
            val errorMessage: String = cause.message ?: "Unknown error"
            call.respond(HttpStatusCode.InternalServerError, JsonErrorResponse(errorMessage))
        }
    }
    routing {
        get("/students") {
            call.respond(usecase.getStudents())
        }

        get("/students/{id}") {
            val id = getId(call.parameters)
            call.respond(usecase.getStudentById(id))
        }

        post("/students") {
            val inputJson = call.receive<Student>()
            usecase.createStudent(inputJson)
            call.respond(JsonResponse("id ${inputJson.id} is created"))
        }

        put("students/{id}") {
            val id = getId(call.parameters)
            val name = call.request.queryParameters["name"]?:throw ValidationError("name is't must be null")
            usecase.updateStudent(Student(id, name))
            call.respond(JsonResponse("id $id is updated"))
        }

        delete("students/{id}") {
            val id = getId(call.parameters)
            usecase.deleteStudent(id)
            call.respond(JsonResponse("id $id is deleted"))
        }
    }
}

private fun getId(parameters: Parameters): Int = runCatching {
    parameters["id"]?.toInt() ?: throw ValidationError("id is't must be null")
}.getOrElse {
    throw ValidationError(it.message ?: "Unkown error")
}