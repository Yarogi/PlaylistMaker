package com.example.playlistmaker.presentation.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.player.api.PlayerInteractor
import com.example.playlistmaker.domain.player.model.PlaybackStatus
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerViewModel(
    val track: Track,
    val playerInteractor: PlayerInteractor,
) : ViewModel() {

    private val trackScreenStateLiveData = MutableLiveData<TrackScreenState>()
    private val playerState = MutableLiveData<TrackPlaybackState>()

    fun trackScreenStateObserver(): LiveData<TrackScreenState> = trackScreenStateLiveData
    fun playerStateObserver(): LiveData<TrackPlaybackState> = playerState

    private var timerJob: Job? = null

    //Добавление в избранное
    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    fun isFavoriteObserver(): LiveData<Boolean> = isFavoriteLiveData
    private val isFavoriteChangeDebouce =
        debounce<Boolean>(delayMillis = 0, viewModelScope, false) { isFavorite ->
            setIsFavoriteValue(isFavorite)
        }

    init {
        trackScreenStateLiveData.postValue(TrackScreenState.Content(track))
        isFavoriteLiveData.postValue(track.isFavorite)
        preparePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    private fun preparePlayer() {

        renderState(TrackPlaybackState.Loading)

        val listener = object : PlayerInteractor.PrepareListener {

            override fun onPrepareListener() {
                renderState(TrackPlaybackState.Ready)
            }

            override fun onCompletionListener() {
                timerJob?.cancel()
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

        timerJob?.cancel()
        renderState(TrackPlaybackState.Paused(getCurrentDuration()))
    }

    private fun startPlayer() {

        playerInteractor.played()
        renderState(TrackPlaybackState.Played(getCurrentDuration()))
        startTimer()

    }

    /** Запуск короутины обновляющей прогресс бар.
     *
     *  Работает только, если плеер в статусе воспроизведения*/
    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.getPlaybackState() == PlaybackStatus.PLAYING) {
                delay(300L)
                renderState(TrackPlaybackState.Played(getCurrentDuration()))
            }
        }
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

    //Установка признака доступности
    fun isFavoriteOnClick() {
        isFavoriteChangeDebouce(!track.isFavorite)
    }

    private fun setIsFavoriteValue(isFavorite: Boolean) {

        viewModelScope.launch {

            withContext(Dispatchers.IO) {
                when (isFavorite) {
                    true -> playerInteractor.addToLibrary(track)
                    false -> playerInteractor.removeFromLibrary(track)
                }
                track.isFavorite = isFavorite
                isFavoriteLiveData.postValue(isFavorite)
            }
        }
    }

}