package com.example.playlistmaker.presentation.playlists.edit

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlists.api.PlaylistEditInteractor
import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.domain.playlists.model.PlaylistEditData
import kotlinx.coroutines.launch

class PlaylistEditViewModel(playlistEditInteractor: PlaylistEditInteractor) :
    PlaylistCreateViewModel(playlistEditInteractor) {

    private var playlistId: Int? = null

    fun fillByPlaylistId(id: Int) {
        playlistId = id
        viewModelScope.launch {
            playlistEditInteractor.getPlaylistById(id)
                .collect {
                    it?.let { fillByPlaylist(it) }
                }

        }

    }

    override fun savePlaylist() {

        playlistId?.let { id ->

            val info = PlaylistEditData(
                id = id,
                name = lastName,
                description = lastDescription,
                cover = lastCover
            )

            viewModelScope.launch {
                playlistEditInteractor.saveEditPlaylistInfo(info)
                    .collect {
                        renderState(PlaylistEditState.Create(it))
                    }
            }

        }

    }

    private fun fillByPlaylist(playlist: Playlist) {
        lastName = playlist.name
        lastDescription = playlist.description
        lastCover = playlist.coverPathUri
        renderLastData()
    }

}