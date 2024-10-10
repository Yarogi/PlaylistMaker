package com.example.playlistmaker.data.media_library.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.media_library.db.dao.PlaylistDao
import com.example.playlistmaker.data.media_library.db.dao.TrackDao
import com.example.playlistmaker.data.media_library.db.entity.FeatureTracksEntity
import com.example.playlistmaker.data.media_library.db.entity.PlaylistEntity
import com.example.playlistmaker.data.media_library.db.entity.PlaylistTracksEntity
import com.example.playlistmaker.data.media_library.db.entity.SearchHistoryEntity
import com.example.playlistmaker.data.media_library.db.entity.TrackEntity

@Database(
    version = 3,
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        SearchHistoryEntity::class,
        FeatureTracksEntity::class,
        PlaylistTracksEntity::class
    ]
)
abstract class TrackDataBase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}