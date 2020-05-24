package com.example

import com.example.gateway.MessageNotFoundException
import com.example.gateway.UserNotFoundException
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.routing.routing
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

data class JsonResponse(val message: String)
data class JsonErrorResponse(val error: String)
data class ResponseId(val id: UUID)
class ValidationError(override val message: String) : Throwable(message)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
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

        exception<MessageNotFoundException> { cause ->
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
}



