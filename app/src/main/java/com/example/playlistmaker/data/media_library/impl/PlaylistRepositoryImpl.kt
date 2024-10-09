package com.example.playlistmaker.data.media_library.impl

import android.net.Uri
import com.example.playlistmaker.data.media_library.db.TrackDataBase
import com.example.playlistmaker.data.media_library.db.entity.PlaylistEntity
import com.example.playlistmaker.data.media_library.mapper.PLaylistDbMapper
import com.example.playlistmaker.data.media_library.storage.FileStorage
import com.example.playlistmaker.domain.media_library.playlists.api.PlaylistRepository
import com.example.playlistmaker.domain.media_library.playlists.model.Playlist
import com.example.playlistmaker.domain.media_library.playlists.model.PlaylistCreateData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val dataBase: TrackDataBase,
    private val fileStorage: FileStorage,
    private val mapper: PLaylistDbMapper,
) : PlaylistRepository {

    override suspend fun createPlaylist(data: PlaylistCreateData): Flow<Playlist> = flow {

        val fileName = data.name.filterNot { it.isWhitespace() }

        val path = fileStorage.saveImage(name = fileName, uri = data.cover)
        val entity = mapper.map(data, path)

        dataBase.playlistDao().insertPlaylist(entity)

        emit(mapper.map(entity, fileStorage.getImageUri(path)))

    }

    override suspend fun getAllPlaylist(): Flow<List<Playlist>> = flow {

        val playlistList = dataBase.playlistDao().getAllPlaylists().map { entity ->
            mapper.map(entity, fileStorage.getImageUri(name = entity.coverLocalPath))
        }

        emit(playlistList)

    }

}