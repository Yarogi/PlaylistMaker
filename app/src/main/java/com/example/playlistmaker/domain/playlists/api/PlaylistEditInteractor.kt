package com.example.playlistmaker.domain.playlists.api

import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.domain.playlists.model.PlaylistCreateData
import kotlinx.coroutines.flow.Flow

interface PlaylistEditInteractor {

    suspend fun createPlaylist(data: PlaylistCreateData): Flow<Playlist>
    suspend fun getPlaylistById(id: Int): Flow<Playlist?>

}