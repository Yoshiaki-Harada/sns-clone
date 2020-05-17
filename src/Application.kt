package com.example

import com.example.domain.*
import com.example.gateway.UserNotFoundException
import com.example.usecase.MessageUsecase
import com.example.usecase.UserUsecase
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.response.respond
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
    val mail: String
)

data class ResponseComment(
    val id: UUID, val text: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val userId: UUID
)

data class ResponseMessage(
    val id: UUID,
    val text: String,
    val userId: UUID,
    val tags: List<ResponseTag>,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val comments: List<ResponseComment>
)

data class ResponseMessages(val messages: List<ResponseMessage>)

data class ResponseTag(val id: UUID, val name: String)
class ValidationError(override val message: String) : Throwable(message)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val userUsecase by Injector.kodein.instance<UserUsecase>()
    val messageUsecase by Injector.kodein.instance<MessageUsecase>()
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(CallLogging) {
    }
    install(Locations) {


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
        @Location("/users/{userId}")
        data class UserLocation(val userId: String)
        get<UserLocation> { params ->
            val id = getId(params.userId)
            call.respond(userUsecase.get(UserId(id)).toResponse())
        }
        @Location("/messages")
        data class MessagesLocation(val order: String = "desc", val by: String = "createdAt")
        get<MessagesLocation> { params ->
            val order = kotlin.runCatching {
                Order.getType(params.order)
            }.onFailure {
                throw ValidationError("order must be in ${Order.getValues()}, but ${params.order}")
            }.getOrThrow()
            val by = kotlin.runCatching {
                By.getType(params.by)
            }.onFailure {
                throw ValidationError("by must be in ${By.getValues()}, but ${params.by}")
            }.getOrThrow()

            val messages = messageUsecase.get(by, order).list.map { it.toResponse() }.let {
                ResponseMessages(it)
            }
            call.respond(messages)
        }
    }
}

private fun getId(id: String): UUID = runCatching {
    UUID.fromString(id)
}.getOrElse {
    throw ValidationError(it.message ?: "Unkown error")
}

fun User.toResponse() = ResponseUser(
    id = id.value,
    name = name.value,
    mail = mail.value
)

fun Message.toResponse() = ResponseMessage(
    id = id.value,
    text = text.value,
    userId = userId.value,
    tags = tags.list.map { it.toResponse() },
    createdAt = createdAt,
    updatedAt = updatedAt,
    comments = comments.list.map { it.toResponse() }
)

fun Comment.toResponse() = ResponseComment(
    id = id.value, text = commentText.value, createdAt = createdAt, updatedAt = updatedAt, userId = userId.value
)

fun Tag.toResponse() = ResponseTag(
    id = id.value, name = name.value
)
