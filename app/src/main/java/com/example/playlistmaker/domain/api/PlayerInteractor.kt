package com.example.playlistmaker.domain.api

import android.media.MediaPlayer
import com.example.playlistmaker.domain.model.PlaybackState
import com.example.playlistmaker.domain.model.Track

interface PlayerInteractor {

    val player: PlayerRepository

    fun prepared(track: Track, listener: PrepareListener)

    fun played()

    fun paused()

    fun release()

    fun getCurrentPosition(): Int

    fun getPlaybackState(): PlaybackState

    interface PrepareListener:PlayerRepository.Listener{}

}