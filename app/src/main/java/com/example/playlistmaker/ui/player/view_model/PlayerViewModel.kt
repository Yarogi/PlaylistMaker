package com.example.playlistmaker.ui.player.view_model

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
import com.example.playlistmaker.domain.player.model.PlaybackStatus
import com.example.playlistmaker.ui.player.model.TrackPlaybackState
import com.example.playlistmaker.ui.player.model.TrackScreenState
import com.google.gson.Gson

class PlayerViewModel(val track: Track, val playerInteractor: PlayerInteractor) : ViewModel() {

    companion object {
        fun getViewModelFactory(trackJson: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val track = Gson().fromJson(trackJson, Track::class.java)
                PlayerViewModel(
                    track = track,
                    playerInteractor = Creator.providePlayerInteractor())
            }
        }

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

    //private val playerInteractor by lazy { Creator.providePlayerInteractor() }
    private val handler by lazy { Handler(Looper.getMainLooper()) }

//    private var playerPrepared = false

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    fun preparePlayer() {

//        if (playerPrepared) return

        renderState(TrackPlaybackState.Loading)

        val listener = object : PlayerInteractor.PrepareListener {

            override fun onPrepareListener() {
//                playerPrepared = true
                renderState(TrackPlaybackState.Ready)
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
        return playerInteractor.getCurrentPosition()
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