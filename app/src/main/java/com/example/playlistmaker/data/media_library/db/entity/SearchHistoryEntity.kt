package com.example.playlistmaker.data.media_library.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey
    var trackID: Int,
    var timestamp: Long = 0L,
)
