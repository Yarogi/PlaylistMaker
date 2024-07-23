package com.example.playlistmaker.ui.player.model

import com.example.playlistmaker.domain.main.model.Track

sealed class TrackScreenState {
    object Loading : TrackScreenState()
    data class Content(val track: Track) : TrackScreenState()
}