package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.api.PlayerRepository
import com.example.playlistmaker.domain.player.model.PlaybackStatus
import com.example.playlistmaker.domain.main.model.Track

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : PlayerRepository {

    private var state = PlaybackStatus.DEFAULT

    override fun prepared(track: Track, listener: PlayerRepository.Listener) {

        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            state = PlaybackStatus.PREPARED
            listener.onPrepareListener()
        }
        mediaPlayer.setOnCompletionListener {
            state = PlaybackStatus.PREPARED
            listener.onCompletionListener()
        }
    }

    override fun played() {
        mediaPlayer.start()
        state = PlaybackStatus.PLAYING
    }

    override fun paused() {
        mediaPlayer.pause()
        state = PlaybackStatus.PAUSED
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentProgress(): Int {
        return when (state) {
            PlaybackStatus.PREPARED, PlaybackStatus.DEFAULT -> 0
            else -> mediaPlayer.getCurrentPosition()
        }

    }

    override fun getCurrentState(): PlaybackStatus {
        return state
    }

}