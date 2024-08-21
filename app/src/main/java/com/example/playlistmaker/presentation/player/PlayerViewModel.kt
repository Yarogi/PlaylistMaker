package com.example.playlistmaker.presentation.player

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.player.api.PlayerInteractor
import com.example.playlistmaker.domain.player.model.PlaybackStatus

class PlayerViewModel(
    val track: Track,
    val playerInteractor: PlayerInteractor,
) : ViewModel() {

    companion object {
        private const val DURATION_DELAY = 300L
        private val DURATION_TOKEN = Any()
    }

    private val trackScreenStateLiveData = MutableLiveData<TrackScreenState>()
    private val playerState = MutableLiveData<TrackPlaybackState>()

    init {
        trackScreenStateLiveData.postValue(TrackScreenState.Content(track))
        preparePlayer()
    }

    fun trackScreenStateObserver(): LiveData<TrackScreenState> = trackScreenStateLiveData
    fun playerStateObserver(): LiveData<TrackPlaybackState> = playerState

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    fun preparePlayer() {

        renderState(TrackPlaybackState.Loading)

        val listener = object : PlayerInteractor.PrepareListener {

            override fun onPrepareListener() {
                renderState(TrackPlaybackState.Ready)
            }

            override fun onCompletionListener() {
                handler.removeCallbacksAndMessages(DURATION_TOKEN)
                renderState(TrackPlaybackState.Ready)
            }

        }
        playerInteractor.prepared(
            track = track,
            listener = listener
        )

    }

    fun changePlayState() {
        playbacklControl()
    }

    fun pausePlayer(сheckPlayback: Boolean = false) {

        if (сheckPlayback && playerInteractor.getPlaybackState() != PlaybackStatus.PLAYING) {
            return
        }

        playerInteractor.paused()
        handler.removeCallbacksAndMessages(DURATION_TOKEN)
        renderState(TrackPlaybackState.Paused(getCurrentDuration()))
    }

    private fun startPlayer() {

        renderState(TrackPlaybackState.Played(getCurrentDuration()))

        val showDurationRunnable = object : Runnable {
            override fun run() {

                renderState(TrackPlaybackState.Played(getCurrentDuration()))

                val postTime = SystemClock.uptimeMillis() + DURATION_DELAY
                handler.postAtTime(this, DURATION_TOKEN, postTime)
            }

        }

        val postTime = SystemClock.uptimeMillis() + DURATION_DELAY
        handler.postAtTime(showDurationRunnable, DURATION_TOKEN, postTime)

        playerInteractor.played()

    }

    private fun renderState(state: TrackPlaybackState) {
        playerState.postValue(state)
    }

    private fun getCurrentDuration(): Int {
        return playerInteractor.getCurrentProgress()
    }

    private fun playbacklControl() {
        when (playerInteractor.getPlaybackState()) {
            PlaybackStatus.PLAYING -> {
                pausePlayer()
            }

            PlaybackStatus.PREPARED, PlaybackStatus.PAUSED -> {
                startPlayer()
            }

            PlaybackStatus.DEFAULT -> {}
        }
    }

}