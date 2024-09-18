package com.example.playlistmaker.presentation.player

sealed interface PlayerFeaturedState {

    object Loading : PlayerFeaturedState
    data class Content(val isFeatured: Boolean) : PlayerFeaturedState

}