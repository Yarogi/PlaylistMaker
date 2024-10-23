package com.example.playlistmaker.domain.featured.api

import com.example.playlistmaker.domain.main.model.Track
import kotlinx.coroutines.flow.Flow

interface FeaturedTracksInteractor {
    suspend fun read(): Flow<List<Track>>
}