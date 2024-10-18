package com.example.playlistmaker.domain.playlists.impl

import com.example.playlistmaker.domain.playlists.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlists.api.PlaylistRepository
import com.example.playlistmaker.domain.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository): PlaylistInteractor {
    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylist()
    }
}