package com.example.playlistmaker.presentation.playlists.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.playlists.api.PlaylistItemInteractor
import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.presentation.playlists.item.model.PlaylistDetailedInfo
import kotlinx.coroutines.launch

class PlaylistItemViewModel(
    private val playlistItemInteractor: PlaylistItemInteractor,
) :
    ViewModel() {

    private var lastPlaylistInfo: PlaylistDetailedInfo? = null

    private val stateLiveData = MutableLiveData<PlaylistItemState>()
    fun stateLiveDataObserver(): LiveData<PlaylistItemState> = stateLiveData

    private val shareStateLiveData = MutableLiveData<PlaylistItemShareState>()
    fun shareStateLiveDataObserver(): LiveData<PlaylistItemShareState> = shareStateLiveData

    fun updatePlaylistInfoById(playlistId: Int, forceMode: Boolean = false) {

        if (lastPlaylistInfo != null && !forceMode) return

        viewModelScope.launch {

            playlistItemInteractor.getPlaylistById(id = playlistId)
                .collect {

                    it?.let { playlist ->

                        playlistItemInteractor.getPlaylistTracks(playlistId = playlistId)
                            .collect {

                                val detailedInfo =
                                    getPlaylistDetaledInfo(playlist = playlist, tracks = it)
                                lastPlaylistInfo = detailedInfo

                                renderState(PlaylistItemState.Content(data = detailedInfo))

                            }

                    } ?: Throwable(message = "Playlist not founded by ID: $playlistId")

                }
        }

    }

    fun updatePlaylistInfoByLast() {
        lastPlaylistInfo?.let { updatePlaylistInfoById(it.id, forceMode = true) }
    }

    fun removeFromPlaylist(track: Track) {

        lastPlaylistInfo?.let { info ->

            viewModelScope.launch {
                playlistItemInteractor
                    .removeTrack(track = track, playlistId = info.id)
                    .collect {
                        updatePlaylistInfoById(playlistId = info.id)
                    }
            }

        }

    }

    fun sharePlaylist() {
        lastPlaylistInfo?.let { info ->
            if (info.tracks.isEmpty()) {
                renderShareState(PlaylistItemShareState.Empty)
            } else {
                playlistItemInteractor.share(info.toString())
                finishShare()
            }
        }
    }

    fun getCommandsList() {
        lastPlaylistInfo?.let { info ->
            renderState(PlaylistItemState.Commands(data = info))
        }
    }

    fun deletePlaylist() {

        lastPlaylistInfo?.let { info ->

            viewModelScope.launch {
                playlistItemInteractor.deletePLaylist(info.id)
                    .collect { success ->
                        if (success) {
                            renderState(PlaylistItemState.Deleted)
                        }
                    }
            }

        }

    }

    fun finishShare() {
        renderShareState(PlaylistItemShareState.None)
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

    private fun renderShareState(state: PlaylistItemShareState) {
        shareStateLiveData.postValue(state)
    }

}