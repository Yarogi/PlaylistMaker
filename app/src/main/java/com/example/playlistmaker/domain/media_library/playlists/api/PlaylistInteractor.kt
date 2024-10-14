package com.example.playlistmaker.domain.media_library.playlists.api

import com.example.playlistmaker.domain.media_library.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getAllPlaylists(): Flow<List<Playlist>>
}