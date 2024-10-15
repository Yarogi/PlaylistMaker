package com.example.playlistmaker.data.media_library.impl

import com.example.playlistmaker.data.db.TrackDataBase
import com.example.playlistmaker.data.db.mapper.PLaylistDbMapper
import com.example.playlistmaker.data.db.mapper.TrackDbMapper
import com.example.playlistmaker.data.media_library.storage.FileStorage
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.playlists.api.PlaylistRepository
import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.domain.playlists.model.PlaylistCreateData
import com.example.playlistmaker.domain.playlists.model.TrackAddToPlaylistResult
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PlaylistRepositoryImpl(
    private val dataBase: TrackDataBase,
    private val fileStorage: FileStorage,
    private val playlistMapper: PLaylistDbMapper,
    private val trackDbMapper: TrackDbMapper,
    private val serializer: Gson,
) : PlaylistRepository {

    override suspend fun createPlaylist(data: PlaylistCreateData): Flow<Playlist> = flow {

        val fileName = data.name.filterNot { it.isWhitespace() }

        val path = fileStorage.saveImage(name = fileName, uri = data.cover)
        val entity = playlistMapper.map(data, path)

        dataBase.playlistDao().insertPlaylist(entity)

        emit(playlistMapper.map(entity, fileStorage.getImageUri(path)))

    }.flowOn(Dispatchers.IO)

    override suspend fun getAllPlaylist(): Flow<List<Playlist>> = flow {

        val playlistList = dataBase.playlistDao().getAllPlaylists().map { entity ->
            playlistMapper.map(entity, fileStorage.getImageUri(name = entity.coverLocalPath))
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

}