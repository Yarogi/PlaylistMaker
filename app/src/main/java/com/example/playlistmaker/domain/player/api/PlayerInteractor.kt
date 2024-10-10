package com.example.playlistmaker.domain.player.api

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.media_library.playlists.model.Playlist
import com.example.playlistmaker.domain.player.model.PlaybackStatus
import kotlinx.coroutines.flow.Flow


interface PlayerInteractor {

    val player: PlayerRepository

    fun prepared(track: Track, listener: PrepareListener)

    fun played()

    fun paused()

    fun release()

    fun getCurrentProgress(): Int

    fun getPlaybackState(): PlaybackStatus

    fun addToLibrary(track: Track)

    fun removeFromLibrary(track: Track)

    suspend fun trackInFavorite(track: Track): Flow<Boolean>

    suspend fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackInPlaylist(track: Track, playlist: Playlist)

    interface PrepareListener : PlayerRepository.Listener {}

}