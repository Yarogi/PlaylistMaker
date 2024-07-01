package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.model.Track

interface SearchHistoryInteractor {
    fun save(searchHistory: ArrayList<Track>):Boolean
    fun read(): ArrayList<Track>
}