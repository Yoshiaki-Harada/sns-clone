package com.example

import com.example.domain.*
import com.example.gateway.UserNotFoundException
import com.example.usecase.UserUsecase
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import org.kodein.di.generic.instance
import java.time.ZonedDateTime
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

data class JsonResponse(val message: String)
data class JsonErrorResponse(val error: String)
data class ResponseUser(
    val id: UUID,
    val name: String,
    val mail: String,
    val messages: List<ResponseMessage>,
    val comment: List<ResponseComment>
)

data class ResponseComment(
    val id: UUID, val text: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime
)

data class ResponseMessage(
    val id: UUID,
    val text: String,
    val tags: List<ResponseTag>,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime
)

data class ResponseTag(val id: UUID, val name: String)
class ValidationError(override val message: String) : Throwable(message)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val usecase by Injector.kodein.instance<UserUsecase>()
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(CallLogging) {


    }
    install(StatusPages) {
        exception<UserNotFoundException> { cause ->
            val errorMessage: String = cause.message ?: "Unknown error"
            call.respond(HttpStatusCode.NotFound, JsonErrorResponse(errorMessage))
        }
        exception<ValidationError> { cause ->
            val errorMessage: String = cause.message
            call.respond(HttpStatusCode.BadRequest, JsonErrorResponse(errorMessage))
        }
        exception<Throwable> { cause ->
            cause.printStackTrace()
            val errorMessage: String = cause.message ?: "Unknown error"
            call.respond(HttpStatusCode.InternalServerError, JsonErrorResponse(errorMessage))
        }
    }
    routing {
        get("/users/{id}") {
            val id = getId(call.parameters)
            call.respond(usecase.get(UserId(id)).toResponse())
        }
    }
}

private fun getId(parameters: Parameters): UUID = runCatching {
    parameters["id"]!!.let {
        UUID.fromString(it)
    }
}.getOrElse {
    throw ValidationError(it.message ?: "Unkown error")
}

fun User.toResponse() = ResponseUser(
    id = id.value,
    name = name.value,
    mail = mail.value,
    messages = messages.list.map { it.toResponse() },
    comment = comments.list.map { it.toResponse() }
)

fun Message.toResponse() = ResponseMessage(
    id = id.value,
    text = text.value,
    tags = tags.list.map { it.toResponse() },
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Comment.toResponse() = ResponseComment(
    id = id.value, text = commentText.value, createdAt = createdAt, updatedAt = updatedAt
)

fun Tag.toResponse() = ResponseTag(
    id = id.value, name = name.value
)
