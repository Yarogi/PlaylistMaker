package com.example.playlistmaker.domain.impl.search

import com.example.playlistmaker.domain.api.search.TracksInteractor
import com.example.playlistmaker.domain.api.search.TracksRepository
import com.example.playlistmaker.domain.model.TrackSearchStructure
import java.util.concurrent.Executors

class TracksInteractorImpl(val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(
        searchStructure: TrackSearchStructure,
        consumer: TracksInteractor.TracksConsumer
    ) {
        executor.execute {
            consumer.consume(repository.searchTrack((searchStructure)))
        }

    }
}