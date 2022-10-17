package com.example.soccer.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiSoccerService {

    private const val BASE_URL = "https://apiv2.allsportsapi.com"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .build()
        )
        .build()

    val soccerApi = retrofit.create<ApiSoccer>()
}