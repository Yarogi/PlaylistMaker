package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.common.model.Track

interface SearchHistoryInteractor {
    fun save(searchHistory: ArrayList<Track>):Boolean
    fun read(): ArrayList<Track>
}