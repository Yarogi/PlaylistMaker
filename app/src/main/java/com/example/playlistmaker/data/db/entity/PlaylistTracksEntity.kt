package com.example.playlistmaker.data.db.entity

import androidx.room.Entity

@Entity(tableName = "playlist_tracks", primaryKeys = ["playlistId", "trackId"])
data class PlaylistTracksEntity(

    var playlistId: Int,
    var trackId: Int,
    var timestamp: Long = 0L,
)