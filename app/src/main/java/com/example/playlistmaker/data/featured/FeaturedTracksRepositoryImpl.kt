package com.example.playlistmaker.data.featured

import com.example.playlistmaker.data.db.TrackDataBase
import com.example.playlistmaker.data.db.mapper.TrackDbMapper
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.featured.api.FeaturedTracksRepository
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
            .findInFeaturedTrackById(id)
            ?.let {
                trackDbMapper.map(it)
            }

    }

}