package com.example.soccer.model.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.soccer.model.data.api.Player
import com.example.soccer.model.data.api.Players

@Dao
interface SoccerDao {

    @Query("SELECT * FROM players")
    suspend fun getAll(): List<Players>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(players: List<Player>)
}