package com.example

import com.example.gateway.MessagePort
import com.example.gateway.MessageRepository
import com.example.gateway.UserPort
import com.example.gateway.UserRepository
import com.example.usecase.MessageUsecase
import com.example.usecase.MessageUsecaseImpl
import com.example.usecase.UserUsecase
import com.example.usecase.UserUsecaseImpl
import org.jetbrains.exposed.sql.Database
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.postgresql.ds.PGSimpleDataSource

object Injector {
    val usecaseModule = Kodein.Module("usecase") {
        bind<MessageUsecase>() with singleton { MessageUsecaseImpl(instance(), instance()) }
        bind<UserUsecase>() with singleton { UserUsecaseImpl(instance()) }
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

    public val kodein = Kodein {
        importAll(usecaseModule, portModule, dataSourceModule)
    }
}