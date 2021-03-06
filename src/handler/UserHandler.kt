package com.example.handler

import com.example.Injector
import com.example.JsonResponse
import com.example.ResponseId
import com.example.domain.Mail
import com.example.domain.User
import com.example.domain.UserId
import com.example.domain.UserName
import com.example.usecase.*
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
        val userGetUseCase by Injector.kodein.instance<UserGetUseCase>()
        val usersGetUseCase by Injector.kodein.instance<UsersGetUseCase>()
        val userCreateUseCase by Injector.kodein.instance<UserCreateUseCase>()
        val userUpdateUseCase by Injector.kodein.instance<UserUpdateUseCase>()
        get("/users") {
            call.respond(usersGetUseCase.execute().list.map(User::toResponse))
        }
        post("/users") {
            val user = call.receive<RequestUser>()
            val id = userCreateUseCase.execute(
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
            call.respond(userGetUseCase.execute(UserId(id)).toResponse())
        }
        @Location("/users/{userId}")
        data class UpdateUserLocation(val userId: String)
        put<UpdateUserLocation> { params ->
            val user = call.receive<RequestUser>()
            userUpdateUseCase.execute(
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

