package com.example.playlistmaker.presentation.player

import com.example.playlistmaker.domain.media_library.playlists.model.Playlist

sealed class PlayerPlaylistState(val listState: Int) {
    class Loading(visibleState: Int) : PlayerPlaylistState(visibleState)
    class Content(val data: List<Playlist>, listState: Int) : PlayerPlaylistState(listState)
}