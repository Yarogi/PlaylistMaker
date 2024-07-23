package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchHistoryRepository
import com.example.playlistmaker.domain.main.model.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository):
    SearchHistoryInteractor {

    override fun save(searchHistory: ArrayList<Track>): Boolean {
        return repository.saveHistory(searchHistory)
    }

    override fun read(): ArrayList<Track> {
        return repository.readHistory()
    }

}