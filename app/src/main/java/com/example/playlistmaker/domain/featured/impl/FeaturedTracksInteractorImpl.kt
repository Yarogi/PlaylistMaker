package com.example.playlistmaker.domain.featured.impl

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.featured.api.FeaturedTracksInteractor
import com.example.playlistmaker.domain.featured.api.FeaturedTracksRepository
import kotlinx.coroutines.flow.Flow

class FeaturedTracksInteractorImpl(private val featuredRepository: FeaturedTracksRepository) :
    FeaturedTracksInteractor {
    override suspend fun read(): Flow<List<Track>> =
        featuredRepository.getTracks()
}