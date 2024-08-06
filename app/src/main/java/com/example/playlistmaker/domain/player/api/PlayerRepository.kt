package com.example.playlistmaker.domain.player.api

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.player.model.PlaybackStatus

interface PlayerRepository {

    fun prepared(track: Track, listener: Listener)
    fun played()
    fun paused()
    fun release()

    fun getCurrentProgress(): Int
    fun getCurrentState(): PlaybackStatus

    interface Listener {
        fun onPrepareListener()
        fun onCompletionListener()
    }

}