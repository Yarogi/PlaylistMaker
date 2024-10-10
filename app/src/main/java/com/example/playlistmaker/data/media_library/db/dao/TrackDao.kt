package com.example.playlistmaker.data.media_library.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.media_library.db.entity.FeatureTracksEntity
import com.example.playlistmaker.data.media_library.db.entity.PlaylistTracksEntity
import com.example.playlistmaker.data.media_library.db.entity.SearchHistoryEntity
import com.example.playlistmaker.data.media_library.db.entity.TimestampTrack
import com.example.playlistmaker.data.media_library.db.entity.TrackEntity

@Dao
interface TrackDao {

    /** Служебная */
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(trackEntity: TrackEntity)

    /** Служебная */
    @Delete(entity = TrackEntity::class)
    fun deleteTrack(trackEntity: TrackEntity)

    /** Служебная */
    @Query("SELECT * FROM track_table")
    fun getAllTracks(): List<TrackEntity>

    /** Служебная */
    @Query("SELECT trackId FROM track_table")
    fun getAllTracksId(): List<Int>

    /** Служебная */
    @Query("SELECT * FROM track_table WHERE trackId = :trackId")
    fun findTrackById(trackId: Int): TrackEntity?

    //** Проверяем наличие ссылок в регистрах */
    @Query(
        "SELECT trackId FROM playlist_tracks WHERE trackId = :trackId " +
                "UNION " +
                "SELECT trackId FROM feature_tracks WHERE trackId = :trackId " +
                "UNION " +
                "SELECT trackId FROM search_history WHERE trackId = :trackId "
    )
    fun trackRefernceList(trackId: Int): List<Int>

    //FEATURED TRACKS

    /** Получить список всех избранных треков со временем добавления */
    @Query("SELECT * FROM feature_tracks LEFT JOIN track_table ON feature_tracks.trackID = track_table.trackId")
    fun getAllFeaturedTracks(): List<TimestampTrack>

    /** Добавить трек в избранное */
    @Transaction
    fun addToFeatured(trackEntity: TrackEntity) {
        insertTrack(trackEntity)
        insertToFeatured(FeatureTracksEntity(trackEntity.trackId, System.currentTimeMillis()))
    }

    /** Удалить трек из избранного */
    @Transaction
    fun removeFromFeatured(trackEntity: TrackEntity) {
        deleteFromFeature(FeatureTracksEntity(trackEntity.trackId))
        if (trackRefernceList(trackEntity.trackId).isEmpty()) {
            deleteTrack(trackEntity)
        }
    }

    /** Трек является избранным (возращается его id, если истина) */
    @Query("SELECT trackId FROM feature_tracks WHERE trackId = :trackId")
    fun findInFeaturedTrackId(trackId: Int): Int?

    /** Служебная */
    @Insert(entity = FeatureTracksEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertToFeatured(featureTracksEntity: FeatureTracksEntity)

    /** Служебная */
    @Delete(entity = FeatureTracksEntity::class)
    fun deleteFromFeature(featureTracksEntity: FeatureTracksEntity)

    //HISTORY

    /** Добавить трек в историю */
    @Transaction
    fun addToHistory(trackEntity: TrackEntity) {
        insertTrack(trackEntity)
        insertToHistory(SearchHistoryEntity(trackEntity.trackId, System.currentTimeMillis()))
    }

    /** Убрать трек из истории */
    @Transaction
    fun removeFromHistory(trackEntity: TrackEntity) {
        deleteFromHistory(SearchHistoryEntity(trackEntity.trackId))
        if (trackRefernceList(trackEntity.trackId).isEmpty()) {
            deleteTrack(trackEntity)
        }
    }

    /** Получить историю */
    @Query("SELECT * FROM search_history LEFT JOIN track_table ON search_history.trackID = track_table.trackId ORDER BY search_history.timestamp DESC")
    fun getAllHistory(): List<TimestampTrack>

    /** Служебная */
    @Insert(entity = SearchHistoryEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertToHistory(entity: SearchHistoryEntity)

    /** Служебная */
    @Delete(entity = SearchHistoryEntity::class)
    fun deleteFromHistory(entity: SearchHistoryEntity)

    //PLAYLIST

    /** Добавить трек в плейлист */
    @Transaction
    fun addToPlaylist(trackEntity: TrackEntity, playlistId: Int) {
        insertTrack(trackEntity)
        insertToPlaylist(
            PlaylistTracksEntity(
                playlistId = playlistId,
                trackId = trackEntity.trackId
            )
        )
    }

    //** Убрать трек из плейлиста */
    @Transaction
    fun removeFromPlaylist(trackEntity: TrackEntity, playlistId: Int) {
        deleteFromPlaylist(
            PlaylistTracksEntity(
                playlistId = playlistId,
                trackId = trackEntity.trackId
            )
        )

        if (trackRefernceList(trackEntity.trackId).isEmpty()) {
            deleteTrack(trackEntity)
        }

    }

    @Query("SELECT * FROM playlist_tracks WHERE trackId = :trackId & playlistId = :playlistId")
    fun findTrackInPlaylist(trackId: Int, playlistId: Int): PlaylistTracksEntity?

    /** Служебная */
    @Insert(entity = PlaylistTracksEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertToPlaylist(entity: PlaylistTracksEntity)

    /** Служебная */
    @Delete(entity = PlaylistTracksEntity::class)
    fun deleteFromPlaylist(entity: PlaylistTracksEntity)

}