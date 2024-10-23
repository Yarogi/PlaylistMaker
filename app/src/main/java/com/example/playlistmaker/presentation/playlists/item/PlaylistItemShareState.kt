package com.example.playlistmaker.presentation.playlists.item

sealed interface PlaylistItemShareState {
    object Empty : PlaylistItemShareState
    object None : PlaylistItemShareState
}