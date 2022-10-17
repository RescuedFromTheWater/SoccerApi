package com.example.soccer.retrofit

import com.example.soccer.model.DetailsSoccer
import com.example.soccer.model.Player
import com.example.soccer.model.SearchSoccer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiSoccer {

    @GET("players")
    fun getPlayers(
        @Query("player_image") playerImage: Int,
        @Query("player_name") playerName: Int
    ): Call<List<Player>>

    @GET("players/{player_name}")
    fun getSoccerDetails(
        @Path("player_name") playerName: String
    ): Call<DetailsSoccer>

    @GET("search/players")
    fun searchSoccer(
        @Query("q") query: String,
        @Query("player_image") playerImage: Int,
        @Query("player_name") playerName: Int
    ): Call<SearchSoccer>
}