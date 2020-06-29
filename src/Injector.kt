package com.example

import com.example.gateway.MessagePort
import com.example.gateway.MessageRepository
import com.example.gateway.UserPort
import com.example.gateway.UserRepository
import com.example.usecase.*
import org.jetbrains.exposed.sql.Database
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.postgresql.ds.PGSimpleDataSource

object Injector {
    val usecaseModule = Kodein.Module("usecase") {
        bind<MessagesGetUseCase>() with singleton { MessagesGetUseCaseImpl(instance()) }
        bind<MessageGetUseCase>() with singleton { MessageGetUseCaseImpl(instance()) }
        bind<MessageCreateUseCase>() with singleton { MessageCreateUseCaseImpl(instance(), instance()) }
        bind<MessageUpdateUseCase>() with singleton { MessageUpdateUseCaseImpl(instance(), instance()) }
        bind<CommentsGetUseCase>() with singleton { CommentsGetUseCaseImpl(instance()) }
        bind<CommentUpdateUseCase>() with singleton { CommentUpdateUseCaseImpl(instance(), instance()) }
        bind<CommentCreateUseCase>() with singleton { CommentCreateUseCaseImpl(instance(), instance()) }
        bind<UserCreateUseCase>() with singleton { UserCreateUseCaseImpl(instance()) }
        bind<UserGetUseCase>() with singleton { UserGetUseCaseImpl(instance()) }
        bind<UsersGetUseCase>() with singleton { UsersGetUseCaseImpl(instance()) }
        bind<UserUpdateUseCase>() with singleton { UserUpdateUseCaseImpl(instance()) }
    }

    val portModule = Kodein.Module("port") {
        bind<UserPort>() with singleton { UserRepository(instance()) }
        bind<MessagePort>() with singleton { MessageRepository(instance()) }
    }

    // ExposedのDatabase接続定義
    val dataSourceModule = Kodein.Module("dataSource") {
        bind<Database>() with singleton {
            val dataSource = PGSimpleDataSource()
            dataSource.user = "developer"
            dataSource.password = "developer"
            dataSource.setURL("jdbc:postgresql://localhost:5432/sns_db")
            Database.connect(dataSource)
        }
    }

    val kodein = Kodein {
        importAll(usecaseModule, portModule, dataSourceModule)
    }
}