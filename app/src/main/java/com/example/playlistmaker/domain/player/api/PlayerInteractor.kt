package com.example.playlistmaker.domain.player.api

import com.example.playlistmaker.domain.player.model.PlaybackStatus
import com.example.playlistmaker.domain.main.model.Track

interface PlayerInteractor {

    val player: PlayerRepository

    fun prepared(track: Track, listener: PrepareListener)

    fun played()

    fun paused()

    fun release()

    fun getCurrentProgress(): Int

    fun getPlaybackState(): PlaybackStatus

    interface PrepareListener: PlayerRepository.Listener {}

}