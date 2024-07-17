package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.search.api.TracksRepository
import com.example.playlistmaker.domain.search.model.TrackSearchStructure
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