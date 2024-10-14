package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.main.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    suspend fun saveHistory(tracks: List<Track>): Flow<Boolean>
    suspend fun readHistory(): Flow<List<Track>>
    suspend fun addToHistory(track: Track): Flow<Boolean>
    suspend fun clearHistory(): Flow<Boolean>
}