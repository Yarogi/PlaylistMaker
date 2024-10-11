package com.example.playlistmaker.data.media_library.impl

import com.example.playlistmaker.data.db.TrackDataBase
import com.example.playlistmaker.data.db.mapper.PLaylistDbMapper
import com.example.playlistmaker.data.db.mapper.TrackDbMapper
import com.example.playlistmaker.data.media_library.storage.FileStorage
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.media_library.playlists.api.PlaylistRepository
import com.example.playlistmaker.domain.media_library.playlists.model.Playlist
import com.example.playlistmaker.domain.media_library.playlists.model.PlaylistCreateData
import com.example.playlistmaker.domain.media_library.playlists.model.TrackAddToPlaylistResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val dataBase: TrackDataBase,
    private val fileStorage: FileStorage,
    private val playlistMapper: PLaylistDbMapper,
    private val trackDbMapper: TrackDbMapper,
) : PlaylistRepository {

    override suspend fun createPlaylist(data: PlaylistCreateData): Flow<Playlist> = flow {

        val fileName = data.name.filterNot { it.isWhitespace() }

        val path = fileStorage.saveImage(name = fileName, uri = data.cover)
        val entity = playlistMapper.map(data, path)

        dataBase.playlistDao().insertPlaylist(entity)

        emit(playlistMapper.map(entity, fileStorage.getImageUri(path)))

    }

    override suspend fun getAllPlaylist(): Flow<List<Playlist>> = flow {

        val playlistList = dataBase.playlistDao().getAllPlaylists().map { entity ->
            playlistMapper.map(entity, fileStorage.getImageUri(name = entity.coverLocalPath))
        }

        emit(playlistList)

    }

    override suspend fun addTrack(
        track: Track,
        playlist: Playlist,
    ): Flow<TrackAddToPlaylistResult> = flow {

        val playlistTrack = dataBase.trackDao().findTrackInPlaylist(
            trackId = track.trackId,
            playlistId = playlist.id
        )

        if (playlistTrack == null) {
            dataBase.trackDao().addToPlaylist(
                trackEntity = trackDbMapper.map(track),
                playlistId = playlist.id
            )
        } else {
            emit(TrackAddToPlaylistResult.ADDED)
        }

    }


}