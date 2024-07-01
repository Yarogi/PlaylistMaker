package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.model.Resource
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.TrackSearchStructure

interface TracksRepository {
    fun searchTrack(searchStructure: TrackSearchStructure): Resource<List<Track>>
}