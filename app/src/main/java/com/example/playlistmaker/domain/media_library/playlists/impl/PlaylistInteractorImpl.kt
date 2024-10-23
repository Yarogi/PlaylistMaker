package com.example.playlistmaker.domain.media_library.playlists.impl

import com.example.playlistmaker.domain.media_library.playlists.api.PlaylistInteractor
import com.example.playlistmaker.domain.media_library.playlists.api.PlaylistRepository
import com.example.playlistmaker.domain.media_library.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository:PlaylistRepository): PlaylistInteractor {
    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylist()
    }
}