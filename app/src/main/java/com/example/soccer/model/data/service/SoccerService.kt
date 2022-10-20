package com.example.soccer.model.data.service

import com.example.soccer.model.data.api.ApiSoccer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object SoccerService {

    private const val BASE_URL = "https://apiv2.allsportsapi.com"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .build()
        )
        .build()

    val apiSoccer = retrofit.create<ApiSoccer>()

    fun provideApiSource(): ApiSoccer = apiSoccer
}