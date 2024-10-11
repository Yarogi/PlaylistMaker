package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.dao.TrackDao
import com.example.playlistmaker.data.db.entity.FeatureTracksEntity
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.PlaylistTracksEntity
import com.example.playlistmaker.data.db.entity.SearchHistoryEntity
import com.example.playlistmaker.data.db.entity.TrackEntity

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