package com.example.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.settings.api.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {

    companion object {
        private const val DARK_THEME_KEY = "dark_mode"
    }

    override fun getThemeSettings(): ThemeSettings {

        val darkMode = sharedPreferences?.getBoolean(DARK_THEME_KEY, false)!!
        return ThemeSettings(darkMode = darkMode)

    }

    override fun updateThemeSetting(settings: ThemeSettings) {

        sharedPreferences.edit()
            ?.putBoolean(DARK_THEME_KEY, settings.darkMode)
            ?.apply()

    }
}