package com.example.playlistmaker.presentation.player

import com.example.playlistmaker.domain.main.model.Track

sealed class TrackScreenState {
    data object Loading : TrackScreenState()
    data class Content(val track: Track) : TrackScreenState()
}