package com.example.playlistmaker.data.media_library.db.entity

import androidx.room.Entity

@Entity(tableName = "playlist_tracks_table")
data class PlaylistTracksEntity(

    var playlistId: Int,
    var trackId: Int,

    )