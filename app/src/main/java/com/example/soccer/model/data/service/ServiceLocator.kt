package com.example.soccer.model.data.service

import android.content.Context
import com.example.soccer.model.data.room.SoccerDao
import com.example.soccer.model.data.room.appDataBase

object ServiceLocator {

    private lateinit var contextApp: Context

    private val soccerDao by lazy {
        contextApp.appDataBase.soccerDao
    }

    fun init(context: Context) {
        this.contextApp = context
    }

    fun provideDataSource(): SoccerDao = soccerDao
}