package com.example.playlistmaker.domain.api.player

import com.example.playlistmaker.domain.model.PlaybackState
import com.example.playlistmaker.domain.model.Track

interface PlayerRepository {

    fun prepared(track: Track, listener: Listener)
    fun played()
    fun paused()
    fun release()

    fun getCurrentPosition(): Int
    fun getCurrentState(): PlaybackState

    interface Listener {
        fun onPrepareListener()
        fun onCompletionListener()
    }

}