package com.example.playlistmaker.data.media_library.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.media_library.db.dao.TrackDao
import com.example.playlistmaker.data.media_library.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class TrackDataBase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}