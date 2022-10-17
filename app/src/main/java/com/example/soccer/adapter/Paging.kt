package com.example.soccer.adapter

sealed class Paging<out T> {
    data class Content<T>(val data: T) : Paging<T>()

    object Loading : Paging<Nothing>()
}