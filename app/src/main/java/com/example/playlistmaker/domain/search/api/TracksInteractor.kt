package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchStructure
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(searchStructure: TrackSearchStructure): Flow<Pair<List<Track>?, String?>>
}