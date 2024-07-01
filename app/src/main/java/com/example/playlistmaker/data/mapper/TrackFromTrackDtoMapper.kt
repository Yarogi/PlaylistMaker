package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.model.Track

object TrackFromTrackDtoMapper {
    fun get(trackDto: TrackDto): Track {
        val track = Track(
            trackId = trackDto.trackId,
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            trackTimeMillis = trackDto.trackTimeMillis,
            artworkUrl100 = trackDto.artworkUrl100,
            collectionName = trackDto.collectionName,
            releaseDate = trackDto.releaseDate,
            primaryGenreName = trackDto.primaryGenreName,
            country = trackDto.country,
            previewUrl = trackDto.previewUrl
        )
        return track
    }
}