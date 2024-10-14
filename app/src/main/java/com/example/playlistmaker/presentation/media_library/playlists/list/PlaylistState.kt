package com.example.playlistmaker.presentation.media_library.playlists.list

import com.example.playlistmaker.domain.media_library.playlists.model.Playlist

sealed interface PlaylistState {

    object Loading : PlaylistState
    data class Content(val data: List<Playlist>) : PlaylistState
    object Empty : PlaylistState
}