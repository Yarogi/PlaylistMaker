package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.model.Track

interface SearchHistoryRepository {
    fun saveHistory(tracks: ArrayList<Track>):Boolean
    fun readHistory(): ArrayList<Track>
}