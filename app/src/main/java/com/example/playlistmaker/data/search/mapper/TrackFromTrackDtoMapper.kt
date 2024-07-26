package com.example.playlistmaker.data.search.mapper

import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.main.model.Track

object TrackFromTrackDtoMapper {
    fun getTrack(trackDto: TrackDto): Track {
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

    fun getTrackList(traksDto:List<TrackDto>):List<Track>{
        return traksDto.map { trackDto ->  getTrack(trackDto) }
    }

}