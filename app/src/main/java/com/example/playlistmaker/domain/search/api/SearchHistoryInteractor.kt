package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.main.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {
    suspend fun save(searchHistory: List<Track>): Flow<Boolean>
    suspend fun read(): Flow<List<Track>>
    suspend fun addToHistory(track: Track): Flow<Boolean>
    suspend fun clearHistory(): Flow<Boolean>
}