package com.example.playlistmaker.presentation.media_library.playlists.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.media_library.playlists.api.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val playlistStateLiveData = MutableLiveData<PlaylistState>()
    fun playlistStateObserver(): LiveData<PlaylistState> = playlistStateLiveData

    fun getPlaylists() {

        renderState(PlaylistState.Loading)
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists()
                .flowOn(Dispatchers.IO)
                .collect { playlists ->
                    if (playlists.isEmpty()) {
                        renderState(PlaylistState.Empty)
                    } else {
                        renderState(PlaylistState.Content(playlists))
                    }
                }
        }


    }


    private fun renderState(state: PlaylistState) {
        playlistStateLiveData.postValue(state)
    }

}