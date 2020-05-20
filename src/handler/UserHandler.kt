package com.example.handler

import com.example.Injector
import com.example.JsonResponse
import com.example.ResponseId
import com.example.domain.Mail
import com.example.domain.User
import com.example.domain.UserId
import com.example.domain.UserName
import com.example.usecase.UserUsecase
import com.example.valueobject.CreatedUser
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.put
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import org.kodein.di.generic.instance
import java.util.*


data class ResponseUser(
    val id: UUID,
    val name: String,
    val mail: String
)

fun Application.userModule() {
    routing {
        val userUsecase by Injector.kodein.instance<UserUsecase>()
        get("/users") {
            call.respond(userUsecase.get().list.map(User::toResponse))
        }
        post("/users") {
            val user = call.receive<RequestUser>()
            val id = userUsecase.create(
                CreatedUser(
                    name = UserName(user.name),
                    mail = Mail(user.mail)
                )
            )
            call.respond(ResponseId(id.value))
        }
        @Location("/users/{userId}")
        data class GetUserLocation(val userId: String)
        get<GetUserLocation> { params ->
            val id = getId(params.userId)
            call.respond(userUsecase.get(UserId(id)).toResponse())
        }
        @Location("/users/{userId}")
        data class UpdateUserLocation(val userId: String)
        put<UpdateUserLocation> { params ->
            val user = call.receive<RequestUser>()
            userUsecase.update(
                UserId(getId(params.userId)),
                CreatedUser(name = UserName(user.name), mail = Mail(user.mail))
            )
            call.respond(JsonResponse("Ok"))
        }
    }
}

fun User.toResponse() = ResponseUser(
    id = id.value,
    name = name.value,
    mail = mail.value
)

