package com.example.playlistmaker.domain.playlists.api

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.playlists.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistItemInteractor {

    suspend fun getPlaylistById(id: Int): Flow<Playlist?>
    suspend fun getPlaylistTracks(playlistId: Int): Flow<List<Track>>

}