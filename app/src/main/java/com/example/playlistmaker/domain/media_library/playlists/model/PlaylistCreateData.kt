package com.example.playlistmaker.domain.media_library.playlists.model

import android.net.Uri

data class PlaylistCreateData(
    val name: String,
    val description: String?,
    val cover: Uri?,
)