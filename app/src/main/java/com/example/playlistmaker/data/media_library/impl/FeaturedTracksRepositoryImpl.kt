package com.example.playlistmaker.data.media_library.impl

import com.example.playlistmaker.data.media_library.db.TrackDataBase
import com.example.playlistmaker.data.media_library.db.entity.FeatureTracksEntity
import com.example.playlistmaker.data.media_library.mapper.TrackDbMapper
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.media_library.favorites.api.FeaturedTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FeaturedTracksRepositoryImpl(
    private val trackDataBase: TrackDataBase,
    private val trackDbMapper: TrackDbMapper,
) : FeaturedTracksRepository {

    override fun addTrack(track: Track) {

        val trackEntity = trackDbMapper.map(track)
        trackDataBase.trackDao().addToFeatured(trackEntity)

    }

    override fun removeTrack(track: Track) {
        val tracksEntity = trackDbMapper.map(track)
        trackDataBase.trackDao().removeFromFeatured(tracksEntity)
    }

    override fun getTracks(): Flow<List<Track>> = flow {

        val result = trackDataBase.trackDao().getAllFeaturedTracks()
            .sortedByDescending { it.timestamp }
            .map { trackDbMapper.map(it) }

        emit(result)

    }

    override suspend fun getTrackById(id: Int): Track? {

        return trackDataBase.trackDao()
            .findTrackById(id)
            ?.let { trackDbMapper.map(it) }

    }

}