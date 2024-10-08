package com.example.playlistmaker.data.media_library.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val name: String,
    val description: String,
    val coverLocalPath: String,
    val tracksId: String,
    val tracksQuantity: Int,
)