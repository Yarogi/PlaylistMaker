package com.example.playlistmaker.presentation.media_library.playlists.edit_playlist

import android.content.ClipDescription
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.media_library.playlists.model.Playlist

class PlayListEditViewModel : ViewModel() {

    private val playListState = MutableLiveData<PlaylistEditState>()
    fun playListStateObserver(): LiveData<PlaylistEditState> = playListState

    private var lastName: String = ""
    private var lastDescription: String = ""

    fun nameChanged(newName: String) {

        if (newName == lastName) return

        lastName = newName
        renderLastData()

    }

    fun descriptionChanged(newDescription: String) {

        if (newDescription == lastDescription) return
        lastDescription = newDescription

        renderLastData()

    }

    private fun renderLastData() {

        renderState(
            PlaylistEditState.Content(
                Playlist(
                    name = lastName,
                    description = lastDescription
                )
            )
        )

    }

    private fun renderState(state: PlaylistEditState) {
        playListState.postValue(state)
    }

}