package com.example.playlistmaker.presentation.media_library.playlists.edit_playlist

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.media_library.playlists.api.PlaylistEditInteractor
import com.example.playlistmaker.domain.media_library.playlists.model.PlaylistCreateData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PlayListEditViewModel(private val playlistEditInteractor: PlaylistEditInteractor) :
    ViewModel() {

    private val playListState = MutableLiveData<PlaylistEditState>(PlaylistEditState.Empty)
    fun playListStateObserver(): LiveData<PlaylistEditState> = playListState

    private var lastName: String = ""
    private var lastDescription: String = ""
    private var lastCover: Uri? = null

    fun onNameChanged(newName: String) {

        if (newName == lastName) return

        lastName = newName
        renderLastData()

    }

    fun onDescriptionChanged(newDescription: String) {

        if (newDescription == lastDescription) return
        lastDescription = newDescription

        renderLastData()

    }

    fun onCoverChanged(newCover: Uri?) {
        if (newCover == lastCover) return

        lastCover = newCover
        renderLastData()

    }

    fun savePlaylist() {

        viewModelScope.launch {

            playlistEditInteractor.createPlaylist(
                PlaylistCreateData(
                    name = lastName,
                    description = lastDescription,
                    cover = lastCover
                )
            ).flowOn(Dispatchers.IO)
                .collect { playlist ->
                    renderState(PlaylistEditState.Create(playlist))
                }

        }

    }

    private fun renderLastData() {

        renderState(
            PlaylistEditState.Content(
                PlaylistCreateData(
                    name = lastName,
                    description = lastDescription,
                    cover = lastCover
                )
            )
        )

    }

    private fun renderState(state: PlaylistEditState) {
        playListState.postValue(state)
    }


}