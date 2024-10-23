package com.example.playlistmaker.presentation.player

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.playlists.model.Playlist

sealed interface PlaylistTrackAddState {
    object Loading : PlaylistTrackAddState
    data class TrackAdded(val track: Track, val playlist: Playlist) : PlaylistTrackAddState
    data class TrackAddedEarly(val track: Track, val playlist: Playlist) : PlaylistTrackAddState
    object Empty : PlaylistTrackAddState
    object Error : PlaylistTrackAddState
}