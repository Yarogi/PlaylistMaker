package com.example.playlistmaker.ui.media_library.view_model

import android.util.Log
import androidx.lifecycle.ViewModel

class PlaylistViewModel : ViewModel() {

    fun createNewPlaylist(){
        Log.d("PLM_DEBUG", "PlaylistViewModel: create new playlist")
    }

}