package com.example.playlistmaker.data.search.storage.impl

import android.content.Context
import com.example.playlistmaker.data.search.storage.HistoryStorage
import com.example.playlistmaker.domain.main.model.Track
import com.google.gson.Gson

class HistoryStorageImpl(context: Context) : HistoryStorage {

    companion object{

        private const val SEARCH_PREFERENCES = "playlistmaker_search_preferences"
        private const val HISTORY_KEY = "search_history"

    }

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

        var json:String? = null
        try {
            json = sharedPreferences?.getString(HISTORY_KEY, null)
        }catch (e:Exception){
            val error = e.message
        }

        if (!json.isNullOrEmpty()) {
            result.addAll(Gson().fromJson(json, Array<Track>::class.java))
        }

        return result

    }


}