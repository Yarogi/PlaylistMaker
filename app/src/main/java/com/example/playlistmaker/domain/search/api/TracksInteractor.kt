package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.model.Resource
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchStructure

interface TracksInteractor {

    fun searchTracks(searchStructure: TrackSearchStructure, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(resource: Resource<List<Track>>)
    }

}