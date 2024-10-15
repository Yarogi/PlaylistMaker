package com.example.playlistmaker.presentation.playlists.item.model

import android.net.Uri
import com.example.playlistmaker.domain.main.model.Track

class PlaylistDetailedInfo(
    val id: Int,
    val name: String,
    val description: String,
    val tracksQuantity: Int,
    val coverPathUri: Uri?,
    val tracks: List<Track>,
) {

    var totalDuration: Float = 0F

    init {

        if (tracks.isNotEmpty()) {
            totalDuration = tracks.map { it.trackTimeMillis }.sum()
        }
    }


}