package com.example.playlistmaker.data.impl

import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.mapper.TrackFromTrackDtoMapper
import com.example.playlistmaker.data.mapper.TrackRequestFromTrackSearchStructureMapper
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.domain.api.search.TracksRepository
import com.example.playlistmaker.domain.model.Resource
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.TrackSearchStructure

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTrack(searchStructure: TrackSearchStructure): Resource<List<Track>> {

        val request = TrackRequestFromTrackSearchStructureMapper.get(searchStructure)

        val response = networkClient.doRequest(request)

        if (response.resultCode == 200) {
            val data = (response as TrackResponse).results.map { TrackFromTrackDtoMapper.get(it) }
            return Resource.Success(data)
        } else {
            return Resource.Error("A network error has occurred")
        }

    }
}