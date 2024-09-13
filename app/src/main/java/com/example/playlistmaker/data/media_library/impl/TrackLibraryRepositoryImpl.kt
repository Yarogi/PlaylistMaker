package com.example.playlistmaker.data.media_library.impl

import com.example.playlistmaker.data.media_library.db.TrackDataBase
import com.example.playlistmaker.data.media_library.mapper.TrackDbMapper
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.media_library.favorites.TrackLibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackLibraryRepositoryImpl(
    private val trackDataBase: TrackDataBase,
    private val trackDbMapper: TrackDbMapper,
) : TrackLibraryRepository {

    override fun addTrack(track: Track) {
        trackDataBase.trackDao().insertTrack(
            trackEntity = trackDbMapper.map(track)
        )
    }

    override fun removeTrack(track: Track) {
        trackDataBase.trackDao().deleteTrack(
            trackEntity = trackDbMapper.map(track)
        )
    }

    override fun getTracks(): Flow<List<Track>> = flow {

        val result = trackDataBase.trackDao().getAllTracks()
        emit(result.map { track -> trackDbMapper.map(track) })

    }

    override fun getAllTrackId(): Flow<List<Int>> = flow {
        val result = trackDataBase.trackDao().getAllTracksId()
        emit(result)
    }
}