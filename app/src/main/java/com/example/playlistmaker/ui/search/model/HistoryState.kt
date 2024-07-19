package com.example.playlistmaker.ui.search.model

import com.example.playlistmaker.domain.main.model.Track

sealed interface HistoryState {

    data class Content(val tracks: List<Track>) : HistoryState
    object Empty : HistoryState
    data class Replace(
        val indexFrom: Int,
        val indexTo: Int,
        val tracks: List<Track>,
    ) : HistoryState
}

