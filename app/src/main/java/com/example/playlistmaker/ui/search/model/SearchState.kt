package com.example.playlistmaker.ui.search.model

import com.example.playlistmaker.domain.main.model.Track

sealed interface SearchState {

    object SearchLoading : SearchState
    object SearchEmpty : SearchState
    object SearchError : SearchState
    data class SearchContent(val tracks: List<Track>) : SearchState

    data class HistoryContent(val tracks: List<Track>) : SearchState
    object HistoryEmpty : SearchState
    data class ReplacedHistory(val indexFrom: Int, val indexTo: Int, val tracks: List<Track>) :
        SearchState


}