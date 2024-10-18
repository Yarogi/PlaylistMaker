package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylistEntity(playlistEntity: PlaylistEntity)

    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId")
    suspend fun deletePlaylistsTracks(playlistId: Int)

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    suspend fun findPlaylistById(playlistId: Int): PlaylistEntity?

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT trackId  FROM playlist_tracks WHERE playlistId = :playlistId")
    suspend fun getPLaylistTrakcsId(playlistId: Int): List<Int>

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Int): Flow<PlaylistEntity?>

    @Query(
        "SELECT * " +
                "FROM playlist_tracks " +
                "   LEFT JOIN track_table " +
                "       ON playlist_tracks.trackId = track_table.trackId  " +
                "WHERE playlist_tracks.playlistId = :playlistId"
    )
    fun getTracks(playlistId: Int): List<TrackEntity>

    @Transaction
    suspend fun deletePlaylistSafety(playlistEntity: PlaylistEntity) {
        deletePlaylistsTracks(playlistEntity.id)
        deletePlaylistEntity(playlistEntity)
    }

}