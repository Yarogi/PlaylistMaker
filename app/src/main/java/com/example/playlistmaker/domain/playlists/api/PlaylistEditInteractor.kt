package com.example.playlistmaker.domain.playlists.api

import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.domain.playlists.model.PlaylistCreateData
import com.example.playlistmaker.domain.playlists.model.PlaylistEditData
import kotlinx.coroutines.flow.Flow

interface PlaylistEditInteractor {

    suspend fun createPlaylist(data: PlaylistCreateData): Flow<Playlist>
    suspend fun saveEditPlaylistInfo(data: PlaylistEditData): Flow<Playlist>
    suspend fun getPlaylistById(id: Int): Flow<Playlist?>

}