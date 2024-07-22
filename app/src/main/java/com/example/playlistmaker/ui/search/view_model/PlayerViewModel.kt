package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.player.api.PlayerInteractor
import com.example.playlistmaker.domain.player.model.PlaybackState
import com.example.playlistmaker.ui.search.model.PlayerState
import com.google.gson.Gson

class PlayerViewModel(val track: Track) : ViewModel() {

    companion object {
        fun getViewModelFactory(trackJson: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val track = Gson().fromJson(trackJson, Track::class.java)
                PlayerViewModel(track)
            }
        }

        private const val DURATION_DELAY = 300L
        private val DURATION_TOKEN = Any()
    }

    private val playerInteractor by lazy { Creator.providePlayerInteractor() }
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    private val trackLiverData = MutableLiveData(track)
    fun trackObserver(): LiveData<Track> = trackLiverData

    private val playerState = MutableLiveData<PlayerState>()
    fun playerStateObserver(): LiveData<PlayerState> = playerState

    private var playerPrepared = false

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    fun preparePlayer() {

        if (playerPrepared) return

        renderState(PlayerState.Loading)

        val listener = object : PlayerInteractor.PrepareListener {

            override fun onPrepareListener() {
                playerPrepared = true
                renderState(PlayerState.Ready)
            }

            override fun onCompletionListener() {
                handler.removeCallbacksAndMessages(DURATION_TOKEN)
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

    fun pausePlayer() {
        playerInteractor.paused()
        handler.removeCallbacksAndMessages(DURATION_TOKEN)
        renderState(PlayerState.Paused(getCurrentDuration()))
    }

    private fun startPlayer() {

        renderState(PlayerState.Played(getCurrentDuration()))

        val showDurationRunnable = object : Runnable {
            override fun run() {
                renderState(PlayerState.Played(getCurrentDuration()))

                val postTime = SystemClock.uptimeMillis() + DURATION_DELAY
                handler.postAtTime(this, DURATION_TOKEN, postTime)
            }

        }

        val postTime = SystemClock.uptimeMillis() + DURATION_DELAY
        handler.postAtTime(showDurationRunnable, DURATION_TOKEN, postTime)

        playerInteractor.played()

    }

    private fun renderState(state: PlayerState) {
        playerState.postValue(state)
    }

    private fun getCurrentDuration(): Int {
        return playerInteractor.getCurrentPosition()
    }

    private fun playbacklControl() {
        when (playerInteractor.getPlaybackState()) {
            PlaybackState.PLAYING -> {
                pausePlayer()
            }

            PlaybackState.PREPARED, PlaybackState.PAUSED -> {
                startPlayer()
            }

            PlaybackState.DEFAULT -> {}
        }
    }

}