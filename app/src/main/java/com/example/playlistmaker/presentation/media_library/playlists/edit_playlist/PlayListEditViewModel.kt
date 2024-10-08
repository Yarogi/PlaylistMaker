package com.example.playlistmaker.presentation.media_library.playlists.edit_playlist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.media_library.playlists.model.PlaylistCreateData

class PlayListEditViewModel : ViewModel() {

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

    fun onCoverChanged(newCover:Uri?){
        if(newCover == lastCover) return

        saveCoverIsStorage(newCover)

        lastCover = newCover
        renderLastData()

    }

    private fun saveCoverIsStorage(data:Uri?){

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