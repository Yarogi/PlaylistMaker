package com.example.playlistmaker.presentation.media_library.playlists.edit_playlist

import com.example.playlistmaker.domain.media_library.playlists.model.Playlist

sealed interface PlaylistEditState {
    object Loading : PlaylistEditState
    data class Content(val data: Playlist) : PlaylistEditState
}