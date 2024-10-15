package com.example.playlistmaker.domain.playlists.impl

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.playlists.api.PlaylistItemInteractor
import com.example.playlistmaker.domain.playlists.api.PlaylistRepository
import com.example.playlistmaker.domain.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistItemInteractorImpl(repository: PlaylistRepository) : PlaylistItemInteractor {
    override suspend fun getPlaylistBiId(id: Int): Flow<Playlist> {
        TODO("Not yet implemented")
    }

    override suspend fun getPlaylistTracks(playlistId: Int): Flow<List<Track>> {
        TODO("Not yet implemented")
    }
}