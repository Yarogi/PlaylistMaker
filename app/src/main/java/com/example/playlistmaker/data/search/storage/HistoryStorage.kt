package com.example.playlistmaker.data.search.storage

import com.example.playlistmaker.domain.main.model.Track

interface HistoryStorage {
    fun save(list: List<Track>): Boolean
    fun read(): List<Track>

    fun add(track: Track)
    fun clean()

}