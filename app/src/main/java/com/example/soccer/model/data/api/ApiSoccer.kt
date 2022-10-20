package com.example.soccer.model.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiSoccer {

    @GET("players")
    fun getPlayers(
        @Query("player_image") playerImage: String,
        @Query("player_name") playerName: String
    ): Result

    @GET("players/{player_name}")
    fun getSoccerDetails(
        @Path("player_name") playerName: String
    ): Players
}