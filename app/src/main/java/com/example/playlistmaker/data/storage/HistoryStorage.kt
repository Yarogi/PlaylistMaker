package com.example.playlistmaker.data.storage

import com.example.playlistmaker.domain.common.model.Track

interface HistoryStorage {
    fun save(list: ArrayList<Track>):Boolean
    fun read():ArrayList<Track>
}