package com.example.playlistmaker.data.playlists.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

class FileStorageImpl(private val context: Context) : FileStorage {

    override suspend fun saveImage(name: String, uri: Uri?): String {

        var fileName = ""

        if (uri != null) {

            fileName = "${name}.jpg"

            val file = File(getDirectory(), fileName)
            writeDataInFile(file, uri)

        }

        return fileName

    }

    override suspend fun getImageUri(name: String?): Uri? {

        return when {
            name.isNullOrEmpty() -> null
            else -> File(getDirectory(), name).toUri()
        }

    }

    private fun writeDataInFile(file: File, uri: Uri) {

        val streamIn = context.contentResolver.openInputStream(uri)
        val streamOut = FileOutputStream(file)

        BitmapFactory
            .decodeStream(streamIn)
            .compress(Bitmap.CompressFormat.JPEG, 30, streamOut)

        streamIn?.close()
        streamOut.close()

    }

    private fun getDirectory(): File {

        //Ищем нужную директорию
        val directory =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
        //Если таковой нет, то создаем
        if (!directory.exists()) directory.mkdirs()

        return directory

    }

}