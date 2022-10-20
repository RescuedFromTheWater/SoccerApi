package com.example.soccer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccer.model.Lce
import com.example.soccer.model.data.api.ApiSoccer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class DetailsViewModel(
    private val apiSoccer: ApiSoccer,
    private val namePlayer: String
) : ViewModel() {

    val detailFragmentFlow = flow {
        delay(2000)
        runCatching {
            apiSoccer.getSoccerDetails(namePlayer)
        }.onSuccess { emit(Lce.ContentSoccer(it))}
            .onFailure { emit(Lce.Error(it)) }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, Lce.Loading)
    }
