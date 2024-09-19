package com.example.playlistmaker.data.search.storage.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.search.storage.HistoryStorage
import com.example.playlistmaker.domain.main.model.Track
import com.google.gson.Gson

class HistoryStorageImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) :
    HistoryStorage {

    companion object {
        private const val HISTORY_KEY = "search_history"
    }

    override fun save(list: ArrayList<Track>): Boolean {
        val json = gson.toJson(list)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()

        return true
    }

    override fun read(): ArrayList<Track> {

        val result = ArrayList<Track>()

        var json: String? = null
        try {
            json = sharedPreferences?.getString(HISTORY_KEY, null)
        } catch (e: Exception) {
            val error = e.message
        }

        if (!json.isNullOrEmpty()) {

            val trackList = gson.fromJson(json, Array<Track>::class.java)
            result.addAll(trackList)
        }

        return result

    }


}