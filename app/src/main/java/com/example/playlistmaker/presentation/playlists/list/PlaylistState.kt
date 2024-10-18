package com.example.playlistmaker.presentation.playlists.list

import com.example.playlistmaker.domain.playlists.model.Playlist

sealed interface PlaylistState {

    object Loading : PlaylistState
    data class Content(val data: List<Playlist>) : PlaylistState
    object Empty : PlaylistState
}