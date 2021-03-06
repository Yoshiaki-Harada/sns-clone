package com.example.handler

import com.example.Injector
import com.example.JsonResponse
import com.example.ResponseId
import com.example.domain.*
import com.example.usecase.*
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
        val messagesGetUseCase by Injector.kodein.instance<MessagesGetUseCase>()
        val messageGetUseCase by Injector.kodein.instance<MessageGetUseCase>()
        val messageCreateUseCase by Injector.kodein.instance<MessageCreateUseCase>()
        val messageUpdateUseCase by Injector.kodein.instance<MessageUpdateUseCase>()
        val commentsGetUseCase by Injector.kodein.instance<CommentsGetUseCase>()
        val commentCreateUseCase by Injector.kodein.instance<CommentCreateUseCase>()
        val commentUpdateUseCase by Injector.kodein.instance<CommentUpdateUseCase>()

        @Location("/messages")
        data class GetMessagesLocation(val order: String = "asc", val by: String = "createdAt")
        get<GetMessagesLocation> { params ->
            val orderBy = OrderBy.of(params.order, params.by)
            val order = orderBy.order
            val by = orderBy.by

            val messages = messagesGetUseCase.execute(by, order).list.map { it.toResponse() }.let {
                ResponseMessages(it)
            }
            call.respond(messages)
        }

        @Location("/messages")
        data class GetMessagesLocationByUserId(
            val userId: String,
            val order: String = "asc",
            val by: String = "createdAt"
        )
        get<GetMessagesLocationByUserId> { params ->
            val userId = params.userId
            val orderBy = OrderBy.of(params.order, params.by)
            val order = orderBy.order
            val by = orderBy.by
            val messages =
                messagesGetUseCase.execute(UserId(getId(userId)), by, order).list.map { it.toResponse() }.let {
                    ResponseMessages(it)
                }
            call.respond(messages)
        }
        post("/messages") {
            val message = call.receive<RequestMessage>()
            val id = messageCreateUseCase.execute(
                CreatedMessage(
                    userId = UserId(getId(message.userId)),
                    text = MessageText(message.text),
                    tags = CreatedTags(message.tags.map { CreatedTag(it.name) })
                )
            )
            call.respond(ResponseId(id.value))
        }
        @Location("/messages/{messageId}")
        data class GetMessageLocation(val messageId: String)
        get<GetMessageLocation> { params ->
            val messageId = params.messageId
            val message = messageGetUseCase.execute(MessageId(getId(messageId))).toResponse()
            call.respond(message)
        }

        @Location("/messages/{messageId}")
        data class PutMessageLocation(val messageId: String)
        put<PutMessageLocation> { params ->
            val messageId = params.messageId
            val message = call.receive<RequestMessage>()
            messageUpdateUseCase.execute(
                MessageId(getId(messageId)),
                CreatedMessage(
                    userId = UserId(getId(message.userId)),
                    text = MessageText(message.text),
                    tags = CreatedTags(message.tags.map { CreatedTag(it.name) })
                )
            )
            call.respond(JsonResponse("OK"))
        }
        @Location("/messages/{messageId}/comments")
        data class GetCommentsLocation(val messageId: String, val order: String = "asc", val by: String = "createdAt")
        get<GetCommentsLocation> { params ->
            val messageId = params.messageId
            val orderBy = OrderBy.of(params.order, params.by)
            val order = orderBy.order
            val by = orderBy.by

            val messages = commentsGetUseCase.execute(MessageId(getId(messageId)), by, order).list.map {
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
            val id = commentCreateUseCase.execute(
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
            commentUpdateUseCase.execute(
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
    id = messageInfo.id.value,
    text = messageInfo.text.value,
    userId = user.id.value,
    tags = messageInfo.tags.list.map { it.toResponse() },
    createdAt = messageInfo.createdAt.toStr(),
    updatedAt = messageInfo.updatedAt.toStr(),
    comments = messageInfo.comments.list.map { it.toResponse() }
)

fun Comment.toResponse() = ResponseComment(
    id = id.value,
    text = commentText.value,
    createdAt = createdAt.toStr(),
    updatedAt = updatedAt.toStr(),
    userId = userId.value
)

fun Tag.toResponse() = ResponseTag(
    id = id.value, name = name.value
)
