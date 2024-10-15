package com.example.playlistmaker.domain.playlists.impl

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.playlists.api.PlaylistItemInteractor
import com.example.playlistmaker.domain.playlists.api.PlaylistRepository
import com.example.playlistmaker.domain.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistItemInteractorImpl(private val repository: PlaylistRepository) :
    PlaylistItemInteractor {
    override suspend fun getPlaylistById(id: Int): Flow<Playlist?> {
        return repository.getPlaylistById(playlistId = id)
    }

    override suspend fun getPlaylistTracks(playlistId: Int): Flow<List<Track>> {
        return repository.getTracks(playlistId = playlistId)
    }
}