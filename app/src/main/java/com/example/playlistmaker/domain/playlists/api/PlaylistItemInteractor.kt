package com.example.playlistmaker.domain.playlists.api

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistItemInteractor {

    fun share(playlistInfo: String)

    suspend fun getPlaylistById(id: Int): Flow<Playlist?>
    suspend fun getPlaylistTracks(playlistId: Int): Flow<List<Track>>
    suspend fun removeTrack(track: Track, playlistId: Int): Flow<Boolean>

}