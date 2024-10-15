package com.example.playlistmaker.domain.playlists.impl

import com.example.playlistmaker.domain.playlists.api.PlaylistEditInteractor
import com.example.playlistmaker.domain.playlists.api.PlaylistRepository
import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.domain.playlists.model.PlaylistCreateData
import kotlinx.coroutines.flow.Flow

class PlaylistEditInteractorImpl(private val repository: PlaylistRepository) :
    PlaylistEditInteractor {
    override suspend fun createPlaylist(data: PlaylistCreateData): Flow<Playlist> {

        return repository.createPlaylist(data)

    }
}