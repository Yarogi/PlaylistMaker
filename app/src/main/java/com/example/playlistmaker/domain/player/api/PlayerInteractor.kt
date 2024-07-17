package com.example.playlistmaker.domain.player.api

import com.example.playlistmaker.domain.player.model.PlaybackState
import com.example.playlistmaker.domain.model.Track

interface PlayerInteractor {

    val player: PlayerRepository

    fun prepared(track: Track, listener: PrepareListener)

    fun played()

    fun paused()

    fun release()

    fun getCurrentPosition(): Int

    fun getPlaybackState(): PlaybackState

    interface PrepareListener: PlayerRepository.Listener {}

}