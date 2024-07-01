package com.example.playlistmaker.data.storage.sharedprefs

import android.content.Context
import com.example.playlistmaker.data.storage.HistoryStorage
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson

private const val SEARCH_PREFERENCES = "playlistmaker_search_preferences"
private const val HISTORY_KEY = "search_history"

class SharedPrefHistoryStorage(context: Context) : HistoryStorage {

    private val sharedPreferences = context.getSharedPreferences(
        SEARCH_PREFERENCES,
        Context.MODE_PRIVATE
    )

    override fun save(list: ArrayList<Track>): Boolean {
        val json = Gson().toJson(list)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()

        return true
    }

    override fun read(): ArrayList<Track> {

        val result = ArrayList<Track>()

        val json = sharedPreferences.getString(HISTORY_KEY, null)
        if (!json.isNullOrEmpty()) {
            result.addAll(Gson().fromJson(json, Array<Track>::class.java))
        }

        return result

    }

}