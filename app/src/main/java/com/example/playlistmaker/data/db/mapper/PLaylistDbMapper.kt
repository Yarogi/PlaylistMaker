package com.example.playlistmaker.data.db.mapper

import android.net.Uri
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.media_library.playlists.model.Playlist
import com.example.playlistmaker.domain.media_library.playlists.model.PlaylistCreateData

class PLaylistDbMapper {
    fun map(data: PlaylistCreateData, coverLocalPath: String): PlaylistEntity {

        return PlaylistEntity(
            name = data.name,
            description = data.description,
            coverLocalPath = coverLocalPath,
            tracksId = "",
            tracksQuantity = 0
        )

    }

    fun map(data: PlaylistEntity, coverPathUri: Uri?): Playlist {

        return Playlist(
            id = data.id,
            name = data.name,
            description = data.description,
            coverPath = data.coverLocalPath,
            tracksQuantity = data.tracksQuantity,
            coverPathUri = coverPathUri
        )
    }
}