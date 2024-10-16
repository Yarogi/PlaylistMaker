package com.example.playlistmaker.domain.playlists.api

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.domain.playlists.model.PlaylistCreateData
import com.example.playlistmaker.domain.playlists.model.TrackAddToPlaylistResult
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun createPlaylist(data: PlaylistCreateData): Flow<Playlist>
    suspend fun getAllPlaylist(): Flow<List<Playlist>>
    suspend fun addTrack(track: Track, playlist: Playlist): Flow<TrackAddToPlaylistResult>

    suspend fun getPlaylistById(playlistId: Int): Flow<Playlist?>
    suspend fun removeTrack(track: Track, playlistId: Int): Flow<Boolean>
    suspend fun getTracks(playlistId: Int): Flow<List<Track>>


}