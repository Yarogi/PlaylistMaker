package com.example.playlistmaker.presentation.playlists.item

import com.example.playlistmaker.presentation.playlists.item.model.PlaylistDetailedInfo

sealed interface PlaylistItemState {
    object Loading : PlaylistItemState
    data class Content(val data: PlaylistDetailedInfo) : PlaylistItemState
    data class Commands(val data: PlaylistDetailedInfo) : PlaylistItemState
    object Deleted : PlaylistItemState
}