package com.example.playlistmaker.domain.search.model

import com.example.playlistmaker.domain.main.model.Track

data class TrackSearchResult(val traks: List<Track>?, val errorMessage: String?)