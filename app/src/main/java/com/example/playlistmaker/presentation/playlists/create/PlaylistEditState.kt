package com.example.playlistmaker.presentation.playlists.create

import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.domain.playlists.model.PlaylistCreateData

sealed interface PlaylistEditState {
    object Loading : PlaylistEditState
    object Empty: PlaylistEditState
    data class Content(val data: PlaylistCreateData) : PlaylistEditState
    data class Create(val data: Playlist): PlaylistEditState
}