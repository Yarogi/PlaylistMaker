package com.example.playlistmaker.presentation.playlists.item.model

import android.net.Uri
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.ui.util.trackDurationToTimeString
import com.example.playlistmaker.ui.util.tracksQuantityToString

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

    override fun toString(): String {

        val builder = StringBuilder()
        builder.append(name)
        if (description.isNotEmpty()) {
            builder.append("\n")
            builder.append(description)
        }
        builder.append("\n")
        builder.append(tracksQuantityToString(tracksQuantity))


        tracks.forEachIndexed { index, track ->
            builder.append("\n")
            builder.append(
                "${index + 1}.${track.artistName} - ${track.trackName} (${
                    trackDurationToTimeString(
                        track.trackTimeMillis
                    )
                })"
            )


        }

        return builder.toString()

    }


}