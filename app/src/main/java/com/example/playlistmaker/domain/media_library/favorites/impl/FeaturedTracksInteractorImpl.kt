package com.example.playlistmaker.domain.media_library.favorites.impl

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.media_library.favorites.api.FeaturedTracksInteractor
import com.example.playlistmaker.domain.media_library.favorites.api.FeaturedTracksRepository
import kotlinx.coroutines.flow.Flow

class FeaturedTracksInteractorImpl(private val featuredRepository: FeaturedTracksRepository) :
    FeaturedTracksInteractor {
    override suspend fun read(): Flow<List<Track>> =
        featuredRepository.getTracks()
}