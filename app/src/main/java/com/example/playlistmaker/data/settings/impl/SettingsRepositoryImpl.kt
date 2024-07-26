package com.example.playlistmaker.data.settings.impl

import android.content.Context
import com.example.playlistmaker.domain.settings.api.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class SettingsRepositoryImpl(val context: Context) : SettingsRepository {

    companion object {
        private const val PLAYLIST_MAKER_PREFERENCE = "playlistmaker_settings_preferences"
        private const val DARK_THEME_KEY = "dark_mode"
    }

    private val sharedPreferences = context.getSharedPreferences(
        PLAYLIST_MAKER_PREFERENCE,
        Context.MODE_PRIVATE
    )

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