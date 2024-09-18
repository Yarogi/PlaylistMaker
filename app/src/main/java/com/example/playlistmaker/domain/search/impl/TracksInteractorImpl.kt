package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.search.api.TracksRepository
import com.example.playlistmaker.domain.search.model.Resource
import com.example.playlistmaker.domain.search.model.TrackSearchResult
import com.example.playlistmaker.domain.search.model.TrackSearchStructure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    override fun searchTracks(
        searchStructure: TrackSearchStructure,
    ): Flow<TrackSearchResult> {

        return repository.searchTrack(searchStructure).map { result ->
            when (result) {
                is Resource.Error -> TrackSearchResult(null, result.message)
                is Resource.Success -> TrackSearchResult(result.data, null)
            }
        }
    }
}