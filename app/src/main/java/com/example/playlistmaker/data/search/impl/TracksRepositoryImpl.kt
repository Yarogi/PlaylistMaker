package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.dto.TrackResponse
import com.example.playlistmaker.data.search.mapper.TrackFromTrackDtoMapper
import com.example.playlistmaker.data.search.mapper.TrackRequestFromTrackSearchStructureMapper
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.domain.api.search.TracksRepository
import com.example.playlistmaker.domain.model.Resource
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.TrackSearchStructure
import com.example.playlistmaker.R

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTrack(searchStructure: TrackSearchStructure): Resource<List<Track>> {

        val request = TrackRequestFromTrackSearchStructureMapper.get(searchStructure)

        val response = networkClient.doRequest(request)

        return when (response.resultCode) {
            200 -> Resource.Success(data = TrackFromTrackDtoMapper.getTrackList((response as TrackResponse).results))
            else -> Resource.Error(R.string.network_error_message.toString())
        }

    }
}