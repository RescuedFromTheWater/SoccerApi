package com.example.soccer.model

sealed class Lce <out T>{
    object Loading: Lce<Nothing>()
    data class ContentSoccer<T>(val listPlayer: T) : Lce<T>()
    data class Error(val throwable: Throwable) : Lce<Nothing>()
}












