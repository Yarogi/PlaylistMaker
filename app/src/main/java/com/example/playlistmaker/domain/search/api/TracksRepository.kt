package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.model.Resource
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchStructure

interface TracksRepository {
    fun searchTrack(searchStructure: TrackSearchStructure): Resource<List<Track>>
}