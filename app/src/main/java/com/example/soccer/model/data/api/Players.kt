package com.example.soccer.model.data.api

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Players(
    @PrimaryKey
    val playerImage: String,
    val playerName: String,
    val playerAge: String,
    val playerMatchPlayed: String,
    val playerNumber: String
)
