package com.example.playlistmaker.data.db.entity


data class TimestampTrack(
    var timestamp: Long = 0L,
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Float,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?,
)