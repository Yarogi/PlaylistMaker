package com.example.playlistmaker.ui.search.model

import com.example.playlistmaker.domain.main.model.Track

sealed interface SearchState {

    //Search
    object Loading : SearchState
    object Empty : SearchState
    object Error : SearchState
    data class Content(val tracks: List<Track>) : SearchState

    //History
    data class HistoryContent(val tracks: List<Track>): SearchState
    data class ReplaceHistory(
        val indexFrom: Int,
        val indexTo: Int,
        val tracks: List<Track>,
    ) : SearchState
}