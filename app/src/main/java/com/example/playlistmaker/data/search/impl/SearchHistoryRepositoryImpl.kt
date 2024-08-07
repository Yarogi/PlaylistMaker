package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.storage.HistoryStorage
import com.example.playlistmaker.domain.search.api.SearchHistoryRepository
import com.example.playlistmaker.domain.main.model.Track

class SearchHistoryRepositoryImpl(private val storage: HistoryStorage) : SearchHistoryRepository {
    override fun saveHistory(tracks: ArrayList<Track>): Boolean {
        return storage.save(tracks)
    }

    override fun readHistory(): ArrayList<Track> {
        return storage.read()
    }

}