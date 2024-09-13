package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.model.TrackSearchResult
import com.example.playlistmaker.domain.search.model.TrackSearchStructure
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(searchStructure: TrackSearchStructure): Flow<TrackSearchResult>
}