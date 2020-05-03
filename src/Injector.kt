package com.example

import com.mysql.cj.jdbc.MysqlDataSource
import io.requery.Persistable
import io.requery.sql.KotlinConfiguration
import io.requery.sql.KotlinEntityDataStore
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object Injector {
    val usecaseModule = Kodein.Module("usecase") {
        bind<StudentUsecase>() with singleton { StudentUsecaseImpl(instance()) }
    }

    val portModule = Kodein.Module("port") {
        bind<StudentPort>() with singleton {StudentRepository(instance(), instance())}
    }

    val daoModule = Kodein.Module("dao") {
        bind<StudentDao>() with singleton { StudentDao() }
    }
    // ExposedのDatabase接続定義
    val dataSourceModule = Kodein.Module("dataSoure") {
        bind<KotlinEntityDataStore<Persistable>>() with singleton {
            val dataSource = MysqlDataSource().apply {
                serverName = "127.0.0.1"
                port = 3306
                user = "root"
                password = "mysql"
                databaseName = "school"
            }
            KotlinEntityDataStore<Persistable>(KotlinConfiguration(dataSource = dataSource, model = Models.DEFAULT))
        }
    }

    val kodein = Kodein {
        importAll(usecaseModule, portModule, daoModule, dataSourceModule)
    }
}