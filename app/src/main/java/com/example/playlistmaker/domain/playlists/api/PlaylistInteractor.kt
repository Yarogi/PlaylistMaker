package com.example.playlistmaker.domain.playlists.api

import com.example.playlistmaker.domain.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getAllPlaylists(): Flow<List<Playlist>>
}