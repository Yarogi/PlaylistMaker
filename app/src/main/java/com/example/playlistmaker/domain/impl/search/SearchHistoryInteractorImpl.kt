package com.example.playlistmaker.domain.impl.search

import com.example.playlistmaker.domain.api.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.search.SearchHistoryRepository
import com.example.playlistmaker.domain.model.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository): SearchHistoryInteractor {

    override fun save(searchHistory: ArrayList<Track>): Boolean {
        return repository.saveHistory(searchHistory)
    }

    override fun read(): ArrayList<Track> {
        return repository.readHistory()
    }

}