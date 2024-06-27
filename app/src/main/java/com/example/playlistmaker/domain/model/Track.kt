package com.example.playlistmaker.domain.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Float,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast("/", "512x512bb.jpg")

    fun getReleaseYear(): String {

        val secondApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date = LocalDate.parse(releaseDate, secondApiFormat)

        return date.year.toString()

    }
}
