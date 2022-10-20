package com.example.soccer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccer.model.Lce
import com.example.soccer.model.data.api.ApiSoccer
import com.example.soccer.model.data.api.Players
import com.example.soccer.model.data.room.SoccerDao
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class SoccerViewModel(
    private val apiSoccer: ApiSoccer,
    private val soccerDao: SoccerDao
) : ViewModel() {

    private var refreshFlow = MutableSharedFlow<Unit>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var queryFlow = MutableStateFlow("")

    val currentLceFlow: Flow<Lce<List<Players>>> = queryFlow.combine(
        refreshFlow
            .map {
                delay(3000)
                runCatching {
                    apiSoccer.getPlayers("", "")
                        .playersResult
                }.onSuccess {
                    Lce.ContentSoccer(it)
                }.onFailure { Lce.Error(it) }
            }) { query, result ->
        result.map {
            it.filter { it.playerName.contains(query, ignoreCase = true) }
        }.fold(onSuccess = {
            soccerDao.insertAll(it)
            Lce.ContentSoccer(it)
        }, onFailure = { Lce.Error(it) })
    }
        .onStart {
            val storedList = soccerDao.getAll()
            val state = if (storedList.isNotEmpty()) {
                Lce.ContentSoccer(storedList)
            } else {
                Lce.Loading
            }
            emit(state)
        }
        .shareIn(
            viewModelScope,
            SharingStarted.Eagerly,
            1
        )

    fun onQueryChanged(query: String) {
        queryFlow.value = query
    }

    fun onLoadMore() {
        refreshFlow.tryEmit(Unit)
    }
}