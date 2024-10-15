package com.example.playlistmaker.domain.playlists.model

import android.net.Uri

data class PlaylistCreateData(
    val name: String,
    val description: String,
    val cover: Uri?,
)