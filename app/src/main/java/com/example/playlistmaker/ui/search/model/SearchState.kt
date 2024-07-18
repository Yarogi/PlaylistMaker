package com.example.playlistmaker.ui.search.model

import com.example.playlistmaker.domain.main.model.Track

sealed interface SearchState {

    object Loading : SearchState

    object Empty : SearchState

    object Error : SearchState

    class History : SearchState

    data class Content(val traks: List<Track>) : SearchState

}