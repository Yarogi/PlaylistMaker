package com.example.playlistmaker.model

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Float,
    val artworkUrl100: String
) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast("/", "512x512bb.jpg")
}
