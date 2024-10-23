package com.example.playlistmaker.data.playlists.impl

import com.example.playlistmaker.data.db.TrackDataBase
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.mapper.PLaylistDbMapper
import com.example.playlistmaker.data.db.mapper.TrackDbMapper
import com.example.playlistmaker.data.playlists.storage.FileStorage
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.playlists.api.PlaylistRepository
import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.domain.playlists.model.PlaylistCreateData
import com.example.playlistmaker.domain.playlists.model.PlaylistEditData
import com.example.playlistmaker.domain.playlists.model.TrackAddToPlaylistResult
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val dataBase: TrackDataBase,
    private val fileStorage: FileStorage,
    private val playlistMapper: PLaylistDbMapper,
    private val trackDbMapper: TrackDbMapper,
    private val serializer: Gson,
) : PlaylistRepository {

    override suspend fun createPlaylist(data: PlaylistCreateData): Flow<Playlist> = flow {

        val path = saveImage(data)
        val entity = playlistMapper.map(
            data = data,
            coverLocalPath = path
        )

        dataBase.playlistDao().insertPlaylist(entity)
        emit(playlistMapper.map(entity, fileStorage.getImageUri(path)))

    }.flowOn(Dispatchers.IO)

    override suspend fun savePlaylistData(data: PlaylistEditData): Flow<Playlist> = flow<Playlist> {

        val playlistEntity = dataBase.playlistDao().findPlaylistById(data.id)
        if (playlistEntity == null) {
            Throwable("PlaylistEntity not find by id = ${data.id}")
        }
        playlistEntity?.let { entity ->

            with(entity) {
                val lastName: String = coverLocalPath
                coverLocalPath = saveImage(data)
                name = data.name
                description = data.description

                deleteImage(lastName)

            }

            dataBase.playlistDao().insertPlaylist(entity)
            emit(playlistMapper.map(entity, fileStorage.getImageUri(entity.coverLocalPath)))

        }

    }.flowOn(Dispatchers.IO)

    override suspend fun deletePlaylist(playlistId: Int): Flow<Boolean> = flow {
        val playlistEntity = dataBase.playlistDao().findPlaylistById(playlistId)
        playlistEntity?.let { entity ->

            val tracks = dataBase.playlistDao().getPLaylistTrakcsId(playlistId = playlistId)

            dataBase.playlistDao().deletePlaylistSafety(entity)
            tracks.forEach { id -> dataBase.trackDao().deleteTrackByIdSafety(id) }

            deleteImage(entity.coverLocalPath)

            emit(true)

        }

    }.flowOn(Dispatchers.IO)

    override suspend fun getAllPlaylist(): Flow<List<Playlist>> = flow {

        val playlistList = dataBase.playlistDao().getAllPlaylists().map { entity ->
            getPlaylistByPlaylistEntity(entity)
        }

        emit(playlistList)

    }.flowOn(Dispatchers.IO)


    override suspend fun addTrack(
        track: Track,
        playlist: Playlist,
    ): Flow<TrackAddToPlaylistResult> = flow {

        val playlistTrack = dataBase.trackDao().findTrackInPlaylist(
            trackId = track.trackId,
            playlistId = playlist.id
        )

        when {
            playlistTrack == null -> {
                dataBase.trackDao().addToPlaylist(
                    trackEntity = trackDbMapper.map(track),
                    playlistId = playlist.id
                )
                updatePlaylistInfoById(playlistId = playlist.id)
                emit(TrackAddToPlaylistResult.ADDED)
            }

            else -> emit(TrackAddToPlaylistResult.ADDED_EARLIER)
        }


    }.flowOn(Dispatchers.IO)


    override suspend fun getPlaylistById(playlistId: Int): Flow<Playlist?> = dataBase.playlistDao()
        .getPlaylistById(playlistId)
        .map { it?.let { getPlaylistByPlaylistEntity(it) } }
        .flowOn(Dispatchers.IO)


    override suspend fun removeTrack(track: Track, playlistId: Int): Flow<Boolean> = flow {
        dataBase.trackDao()
            .removeFromPlaylist(trackEntity = trackDbMapper.map(track), playlistId = playlistId)
        updatePlaylistInfoById(playlistId)
        emit(true)
    }.flowOn(Dispatchers.IO)

    override suspend fun getTracks(playlistId: Int): Flow<List<Track>> = flow {

        val result = dataBase.playlistDao().getTracks(playlistId = playlistId)
            .map { entity -> trackDbMapper.map(entity) }

        emit(result)
    }.flowOn(Dispatchers.IO)


    private suspend fun updatePlaylistInfoById(playlistId: Int) {

        dataBase.playlistDao().findPlaylistById(playlistId)?.let { entity ->
            val tracksId = dataBase.playlistDao().getPLaylistTrakcsId(entity.id)

            if (entity.tracksQuantity != tracksId.size) {

                entity.tracksQuantity = tracksId.size
                entity.tracksId = serializer.toJson(tracksId)

                dataBase.playlistDao().insertPlaylist(entity)
            }


        }

    }

    private suspend fun getPlaylistByPlaylistEntity(playlistEntity: PlaylistEntity): Playlist {

        return playlistMapper.map(
            playlistEntity,
            fileStorage.getImageUri(playlistEntity.coverLocalPath)
        )
    }

    private suspend fun saveImage(data: PlaylistCreateData): String {

        if (data.cover == null) return ""

        val fileName = data.name.filterNot { it.isWhitespace() }.plus(System.currentTimeMillis())
        val path = fileStorage.saveImage(name = fileName, uri = data.cover)
        return path

    }

    private suspend fun deleteImage(path: String) {

        if (path.isNotEmpty()) {
            fileStorage.deleteImage(path)
        }

    }


}