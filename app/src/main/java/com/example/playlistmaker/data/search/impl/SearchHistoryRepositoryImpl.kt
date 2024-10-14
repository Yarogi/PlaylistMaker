package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.storage.HistoryStorage
import com.example.playlistmaker.domain.search.api.SearchHistoryRepository
import com.example.playlistmaker.domain.main.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchHistoryRepositoryImpl(private val storage: HistoryStorage) : SearchHistoryRepository {
    override suspend fun saveHistory(tracks: List<Track>): Flow<Boolean> = flow {
        storage.save(tracks)
        emit(true)
    }.flowOn(Dispatchers.IO)

    override suspend fun readHistory(): Flow<List<Track>> = flow {
        emit(storage.read())
    }.flowOn(Dispatchers.IO)

    override suspend fun addToHistory(track: Track) = flow {
        storage.add(track)
        emit(true)
    }.flowOn(Dispatchers.IO)

    override suspend fun clearHistory() = flow {
        storage.clean()
        emit(true)
    }.flowOn(Dispatchers.IO)


}