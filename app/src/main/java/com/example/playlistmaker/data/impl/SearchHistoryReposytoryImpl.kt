package com.example.playlistmaker.data.impl

import com.example.playlistmaker.data.storage.HistoryStorage
import com.example.playlistmaker.domain.api.search.SearchHistoryRepository
import com.example.playlistmaker.domain.model.Track

class SearchHistoryReposytoryImpl(private val storage: HistoryStorage) : SearchHistoryRepository {
    override fun saveHistory(tracks: ArrayList<Track>): Boolean {
        return storage.save(tracks)
    }

    override fun readHistory(): ArrayList<Track> {
        return storage.read()
    }

}