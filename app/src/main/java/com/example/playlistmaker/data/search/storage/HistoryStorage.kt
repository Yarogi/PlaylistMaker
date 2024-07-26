package com.example.playlistmaker.data.search.storage

import com.example.playlistmaker.domain.main.model.Track

interface HistoryStorage {
    fun save(list: ArrayList<Track>):Boolean
    fun read():ArrayList<Track>
}