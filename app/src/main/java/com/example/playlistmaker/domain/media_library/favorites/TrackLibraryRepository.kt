package com.example.playlistmaker.domain.media_library.favorites

import com.example.playlistmaker.domain.main.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackLibraryRepository {
    fun addTrack(track: Track)
    fun removeTrack(track: Track)
    fun getTracks(): Flow<List<Track>>
    fun getAllTrackId(): Flow<List<Int>>
}