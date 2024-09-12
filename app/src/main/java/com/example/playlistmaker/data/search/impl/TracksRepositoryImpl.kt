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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTrack(searchStructure: TrackSearchStructure): Flow<Resource<List<Track>>> =
        flow {

            val request = TrackRequestFromTrackSearchStructureMapper.get(searchStructure)
            val response = networkClient.doRequest(request)

            when (response.resultCode) {
                200 -> emit(Resource.Success(data = TrackFromTrackDtoMapper.getTrackList((response as TrackResponse).results)))
                else -> emit(Resource.Error(R.string.network_error_message.toString()))
            }

        }
}