package com.example.playlistmaker.domain.playlists.model

import android.net.Uri

class PlaylistEditData(
    val id: Int, name: String,
    description: String,
    cover: Uri?,
) : PlaylistCreateData(name = name, description = description, cover = cover) {
}