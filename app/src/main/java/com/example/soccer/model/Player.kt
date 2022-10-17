package com.example.soccer.model

import com.google.gson.annotations.SerializedName

data class Player(
    @SerializedName("player_age")
    val playerAge: String,
    @SerializedName("player_image")
    val playerImage: String,
    @SerializedName("player_match_played")
    val playerMatchPlayed: String,
    @SerializedName("player_name")
    val playerName: String,
    @SerializedName("player_number")
    val playerNumber: String
)