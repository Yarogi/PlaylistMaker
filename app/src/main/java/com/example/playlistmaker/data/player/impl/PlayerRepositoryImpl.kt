package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.api.PlayerRepository
import com.example.playlistmaker.domain.player.model.PlaybackState
import com.example.playlistmaker.domain.model.Track

class PlayerRepositoryImpl : PlayerRepository {

    private val mediaPlayer = MediaPlayer()
    private var state = PlaybackState.DEFAULT

    override fun prepared(track: Track, listener: PlayerRepository.Listener) {

        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            state = PlaybackState.PREPARED
            listener.onPrepareListener()
        }
        mediaPlayer.setOnCompletionListener {
            state = PlaybackState.PREPARED
            listener.onCompletionListener()
        }
    }

    override fun played() {
        mediaPlayer.start()
        state = PlaybackState.PLAYING
    }

    override fun paused() {
        mediaPlayer.pause()
        state = PlaybackState.PAUSED
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.getCurrentPosition()
    }

    override fun getCurrentState(): PlaybackState {
        return state
    }

}