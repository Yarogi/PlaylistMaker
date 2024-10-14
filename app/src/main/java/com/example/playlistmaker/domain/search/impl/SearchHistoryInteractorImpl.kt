package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override suspend fun save(searchHistory: List<Track>): Flow<Boolean> {
        return repository.saveHistory(searchHistory)
    }

    override suspend fun read(): Flow<List<Track>> {
        return repository.readHistory()
    }

    override suspend fun addToHistory(track: Track): Flow<Boolean> {
        return repository.addToHistory(track)
    }

    override suspend fun clearHistory(): Flow<Boolean> {
        return repository.clearHistory()
    }


}