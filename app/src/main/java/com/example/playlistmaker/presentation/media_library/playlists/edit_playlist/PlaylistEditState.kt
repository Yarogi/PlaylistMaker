package com.example.playlistmaker.presentation.media_library.playlists.edit_playlist

import com.example.playlistmaker.domain.media_library.playlists.model.PlaylistCreateData

sealed interface PlaylistEditState {
    object Loading : PlaylistEditState
    object Empty: PlaylistEditState
    data class Content(val data: PlaylistCreateData) : PlaylistEditState
}