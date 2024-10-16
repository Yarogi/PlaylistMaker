package com.example.playlistmaker.domain.playlists.impl

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.playlists.api.PlaylistItemInteractor
import com.example.playlistmaker.domain.playlists.api.PlaylistRepository
import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.domain.sharing.api.ExternalNavigator
import kotlinx.coroutines.flow.Flow

class PlaylistItemInteractorImpl(
    private val repository: PlaylistRepository,
    private val externalNavigator: ExternalNavigator,
) :
    PlaylistItemInteractor {
    override fun share(playlistInfo: String) {
        externalNavigator.shareLink(playlistInfo)
    }

    override suspend fun getPlaylistById(id: Int): Flow<Playlist?> {
        return repository.getPlaylistById(playlistId = id)
    }

    override suspend fun getPlaylistTracks(playlistId: Int): Flow<List<Track>> {
        return repository.getTracks(playlistId = playlistId)
    }

    override suspend fun removeTrack(track: Track, playlistId: Int): Flow<Boolean> {
        return repository.removeTrack(track = track, playlistId = playlistId)
    }

}