package com.example.playlistmaker.domain.media_library.playlists.api

import com.example.playlistmaker.domain.media_library.playlists.model.Playlist
import com.example.playlistmaker.domain.media_library.playlists.model.PlaylistCreateData
import kotlinx.coroutines.flow.Flow

interface PlaylistEditInteractor {

    suspend fun createPlaylist(data: PlaylistCreateData): Flow<Playlist>

}