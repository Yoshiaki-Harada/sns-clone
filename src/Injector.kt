package com.example

import com.example.driver.dao.CommentDao
import com.example.driver.dao.MessageDao
import com.example.driver.dao.TagDao
import com.example.driver.dao.UserDao
import com.example.gateway.UserPort
import com.example.gateway.UserRepository
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
        bind<UserUsecase>() with singleton { UserUsecaseImpl(instance()) }
    }

    val portModule = Kodein.Module("port") {
        bind<UserPort>() with singleton { UserRepository(instance(), instance(), instance(), instance()) }
    }

    val daoModule = Kodein.Module("dao") {
        bind<UserDao>() with singleton { UserDao() }
        bind<MessageDao>() with singleton { MessageDao() }
        bind<CommentDao>() with singleton { CommentDao() }
        bind<TagDao>() with singleton { TagDao() }
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
        importAll(usecaseModule, portModule, daoModule, dataSourceModule)
    }
}