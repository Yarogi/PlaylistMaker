package com.example.playlistmaker.domain.player.api

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.player.model.PlaybackStatus

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

    interface PrepareListener : PlayerRepository.Listener {}

}