package com.example.playlistmaker.domain.featured.api

import com.example.playlistmaker.domain.main.model.Track
import kotlinx.coroutines.flow.Flow

interface FeaturedTracksRepository {
    fun addTrack(track: Track)
    fun removeTrack(track: Track)
    fun getTracks(): Flow<List<Track>>
    suspend fun getTrackById(id: Int): Track?
}