package com.example.playlistmaker.domain.api.search

import com.example.playlistmaker.domain.model.Resource
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.TrackSearchStructure

interface TracksInteractor {

    fun searchTracks(searchStructure: TrackSearchStructure, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(resource: Resource<List<Track>>)
    }

}