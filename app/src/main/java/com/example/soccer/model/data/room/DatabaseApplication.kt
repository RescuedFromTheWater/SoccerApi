package com.example.soccer.model.data.room

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.soccer.model.data.service.ServiceLocator

class DataBaseApplication : Application() {

    var database: AppDataBase? = null

    override fun onCreate() {
        super.onCreate()

        ServiceLocator.init(this)
        database = Room.databaseBuilder(
            this,
            AppDataBase::class.java,
            "playersDataBase"
        ).build()
    }
}

val Context.appDataBase: AppDataBase
    get() = when (this) {
        is DataBaseApplication -> requireNotNull(database)
        else -> applicationContext.appDataBase
    }