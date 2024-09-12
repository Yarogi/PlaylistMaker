package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.search.model.Resource
import com.example.playlistmaker.domain.search.model.TrackSearchStructure
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTrack(searchStructure: TrackSearchStructure): Flow<Resource<List<Track>>>
}