package com.example.handler

import com.example.Injector
import com.example.JsonResponse
import com.example.ResponseId
import com.example.domain.*
import com.example.usecase.MessageUsecase
import com.example.valueobject.CreatedComment
import com.example.valueobject.CreatedMessage
import com.example.valueobject.CreatedTag
import com.example.valueobject.CreatedTags
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.locations.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import org.kodein.di.generic.instance
import java.util.*

data class ResponseComment(
    val id: UUID, val text: String,
    val createdAt: String,
    val updatedAt: String,
    val userId: UUID
)

data class ResponseMessage(
    val id: UUID,
    val text: String,
    val userId: UUID,
    val tags: List<ResponseTag>,
    val createdAt: String,
    val updatedAt: String,
    val comments: List<ResponseComment>
)

data class ResponseComments(val comments: List<ResponseComment>)
data class ResponseMessages(val messages: List<ResponseMessage>)
data class ResponseTag(val id: UUID, val name: String)
data class RequestUser(val name: String, val mail: String)
data class RequestMessage(
    val text: String,
    val tags: List<RequestTag>,
    val userId: String
)

data class RequestTag(val name: String)

data class RequestComment(
    val text: String,
    val userId: String
)

@KtorExperimentalLocationsAPI
fun Application.messageModule() {
    routing {
        val messageUsecase by Injector.kodein.instance<MessageUsecase>()

        @Location("/messages")
        data class MessagesLocation(val order: String = "asc", val by: String = "createdAt")
        get<MessagesLocation> { params ->
            val orderBy = OrderBy.of(params.order, params.by)
            val order = orderBy.order
            val by = orderBy.by

            val messages = messageUsecase.get(by, order).list.map { it.toResponse() }.let {
                ResponseMessages(it)
            }
            call.respond(messages)
        }

        @Location("/messages")
        data class MessagesLocationByUserId(
            val userId: String,
            val order: String = "asc",
            val by: String = "createdAt"
        )
        get<MessagesLocationByUserId> { params ->
            val userId = params.userId
            val orderBy = OrderBy.of(params.order, params.by)
            val order = orderBy.order
            val by = orderBy.by
            val messages =
                messageUsecase.getByUerId(UserId(getId(userId)), by, order).list.map { it.toResponse() }.let {
                    ResponseMessages(it)
                }
            call.respond(messages)
        }
        post("/messages") {
            val message = call.receive<RequestMessage>()
            val id = messageUsecase.create(
                CreatedMessage(
                    userId = UserId(getId(message.userId)),
                    text = MessageText(message.text),
                    tags = CreatedTags(message.tags.map { CreatedTag(it.name) })
                )
            )
            call.respond(ResponseId(id.value))
        }
        @Location("/messages/{messageId}/comments")
        data class GetCommentsLocation(val messageId: String)
        get<GetCommentsLocation> { params ->
            val messageId = params.messageId
            val messages = messageUsecase.getComments(MessageId(getId(messageId))).list.map {
                it.toResponse()
            }.let {
                ResponseComments(it)
            }
            call.respond(messages)
        }
        @Location("/messages/{messageId}/comments")
        data class PostComment(val messageId: String)
        post<PostComment> { params ->
            val messageId = params.messageId
            val comment = call.receive<RequestComment>()
            val id = messageUsecase.createComment(
                CreatedComment(
                    MessageId(getId(messageId)),
                    UserId(getId(comment.userId)),
                    CommentText(comment.text)
                )
            )
            call.respond(ResponseId(id.value))
        }
        @Location("/messages/{messageId}/comments/{commentId}")
        data class PutCommentLocation(val messageId: String, val commentId: String)
        put<PutCommentLocation> { params ->
            val messageId = params.messageId
            val commentId = params.messageId
            val comment = call.receive<RequestComment>()
            messageUsecase.updateComment(
                CommentId(getId(commentId)),
                CreatedComment(
                    MessageId(getId(messageId)),
                    UserId(getId(comment.userId)),
                    CommentText(comment.text)
                )
            )
            call.respond(JsonResponse("Ok"))
        }
    }
}


fun Message.toResponse() = ResponseMessage(
    id = id.value,
    text = text.value,
    userId = userId.value,
    tags = tags.list.map { it.toResponse() },
    createdAt = createdAt.toStr(),
    updatedAt = updatedAt.toStr(),
    comments = comments.list.map { it.toResponse() }
)

fun Comment.toResponse() = ResponseComment(
    id = id.value, text = commentText.value, createdAt = createdAt.toStr(), updatedAt = updatedAt.toStr(), userId = userId.value
)

fun Tag.toResponse() = ResponseTag(
    id = id.value, name = name.value
)
