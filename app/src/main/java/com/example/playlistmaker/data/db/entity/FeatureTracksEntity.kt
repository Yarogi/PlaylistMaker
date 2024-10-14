package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feature_tracks")
data class FeatureTracksEntity(
    @PrimaryKey
    var trackId: Int,
    var timestamp: Long = 0L,
)