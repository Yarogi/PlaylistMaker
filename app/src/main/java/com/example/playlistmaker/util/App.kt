package com.example.playlistmaker.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class App : Application() {

    lateinit var settings: ThemeSettings

    override fun onCreate() {
        super.onCreate()

        val settingsInteractor = Creator.provideSettingsInteractor(this)

        settings = settingsInteractor.getThemeSettings()
        switchTheme(settings)

    }

    fun switchTheme(newSettings: ThemeSettings) {

        settings = newSettings

        val darkThemeEnabled = newSettings.darkMode

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

    }


}