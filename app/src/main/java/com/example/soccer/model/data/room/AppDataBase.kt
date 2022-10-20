package com.example.soccer.model.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.soccer.model.data.api.Players

@Database(entities = [Players::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract val soccerDao : SoccerDao
}