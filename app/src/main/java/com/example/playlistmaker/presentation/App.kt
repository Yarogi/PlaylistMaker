package com.example.playlistmaker.presentation

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        switchTheme(sharedPref()?.getBoolean(DARK_THEME_KEY, false)!!)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {

        darkTheme = darkThemeEnabled
        sharedPref()?.edit()
            ?.putBoolean(DARK_THEME_KEY, darkTheme)
            ?.apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

    }

    private fun sharedPref(): SharedPreferences? {
        return getSharedPreferences(PLAYLIST_MAKER_PREFERENCE, MODE_PRIVATE)
    }

    companion object {
        const val PLAYLIST_MAKER_PREFERENCE = "playlist_maker_preference"
        const val DARK_THEME_KEY = "mode_key"
    }

}