package com.example.soccer.model.data.api

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("result")
    val playersResult: List<Player>
)