package com.example.playlistmaker.presentation.playlists.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.playlists.api.PlaylistItemInteractor
import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.presentation.playlists.item.model.PlaylistDetailedInfo
import kotlinx.coroutines.launch

class PlaylistItemViewModel(private val playlistItemInteractor: PlaylistItemInteractor) :
    ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistItemState>()
    fun stateLiveDataObserver(): LiveData<PlaylistItemState> = stateLiveData

    fun updatePlaylistInfoById(playlistId: Int) {

        //Отладка для проверки работы (Удалить)
        val id = playlistId

        viewModelScope.launch {

            playlistItemInteractor.getPlaylistById(id = id)
                .collect {

                    it?.let { playlist ->

                        playlistItemInteractor.getPlaylistTracks(playlistId = id)
                            .collect {

                                val detailedInfo =
                                    getPlaylistDetaledInfo(playlist = playlist, tracks = it)
                                renderState(PlaylistItemState.Content(data = detailedInfo))

                            }

                    } ?: Throwable(message = "Playlist not founded by ID: $id")

                }
        }

    }

    private fun getPlaylistDetaledInfo(
        playlist: Playlist,
        tracks: List<Track>,
    ): PlaylistDetailedInfo {
        return PlaylistDetailedInfo(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            tracksQuantity = playlist.tracksQuantity,
            coverPathUri = playlist.coverPathUri,
            tracks = tracks
        )
    }

    private fun renderState(state: PlaylistItemState) {
        stateLiveData.postValue(state)
    }

}