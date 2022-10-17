package com.example.soccer.model

import com.google.gson.annotations.SerializedName

data class SearchSoccer (
    @SerializedName("player_name")
    val playerName: String,
    val items: List<Player>
)