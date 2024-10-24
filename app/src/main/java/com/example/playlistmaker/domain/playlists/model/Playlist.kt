package com.example.playlistmaker.domain.playlists.model

import android.net.Uri

data class Playlist(
    val id: Int,
    val name: String,
    val description: String,
    val coverPath: String,
    val tracksQuantity:Int,
    val coverPathUri: Uri?
)
