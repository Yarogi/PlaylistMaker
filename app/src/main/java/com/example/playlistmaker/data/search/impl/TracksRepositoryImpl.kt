package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.dto.TrackResponse
import com.example.playlistmaker.data.search.mapper.TrackFromTrackDtoMapper
import com.example.playlistmaker.data.search.mapper.TrackRequestFromTrackSearchStructureMapper
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.domain.search.api.TracksRepository
import com.example.playlistmaker.domain.search.model.Resource
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchStructure
import com.example.playlistmaker.R
import com.example.playlistmaker.data.db.TrackDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.sql.DatabaseMetaData


class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val dataBase: TrackDataBase,
) : TracksRepository {

    override fun searchTrack(searchStructure: TrackSearchStructure): Flow<Resource<List<Track>>> =
        flow {


            val request = TrackRequestFromTrackSearchStructureMapper.get(searchStructure)
            val response = networkClient.doRequest(request)

            when (response.resultCode) {
                200 -> {
                    val favoritesId = dataBase.trackDao().getAllTracksId()
                    val foundTracks =
                        TrackFromTrackDtoMapper.getTrackList((response as TrackResponse).results)
                    foundTracks.forEach { track ->
                        track.isFavorite =
                            favoritesId.find { id -> track.trackId == id } != null
                    }
                    foundTracks.sortedByDescending { track -> track.isFavorite }
                    emit(Resource.Success(data = foundTracks))
                }

                else -> emit(Resource.Error(R.string.network_error_message.toString()))

            }

        }.flowOn(Dispatchers.IO)

}

