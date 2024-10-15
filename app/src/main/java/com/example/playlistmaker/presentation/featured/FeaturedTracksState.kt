package com.example.playlistmaker.presentation.featured

import com.example.playlistmaker.domain.main.model.Track

sealed interface FeaturedTracksState {
    object Loading : FeaturedTracksState
    data class Content(val data: List<Track>) : FeaturedTracksState
    object Empty : FeaturedTracksState
}