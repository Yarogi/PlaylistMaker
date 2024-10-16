package com.example.playlistmaker.data.playlists.storage

import android.net.Uri

interface FileStorage {
    suspend fun saveImage(name: String, uri: Uri?): String
    suspend fun getImageUri(name: String?): Uri?
}