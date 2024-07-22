package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.main.model.Track

interface SearchHistoryRepository {
    fun saveHistory(tracks: ArrayList<Track>):Boolean
    fun readHistory(): ArrayList<Track>
}