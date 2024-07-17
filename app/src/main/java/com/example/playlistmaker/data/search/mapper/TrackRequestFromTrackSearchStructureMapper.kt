package com.example.playlistmaker.data.search.mapper

import com.example.playlistmaker.data.search.dto.TrackRequest
import com.example.playlistmaker.domain.model.TrackSearchStructure

object TrackRequestFromTrackSearchStructureMapper {
    fun get(searchStructure: TrackSearchStructure): TrackRequest {
        return TrackRequest(text = searchStructure.term)
    }
}