package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class App : Application() {

    lateinit var settings: ThemeSettings

    //val settingsInteractor by lazy { Creator.provideSettingsInteractor(applicationContext) }

    override fun onCreate() {

        super.onCreate()
        settings = ThemeSettings(false)

        switchTheme(settings.darkMode)

    }

    fun switchTheme(darkThemeEnabled: Boolean) {

        settings.darkMode = darkThemeEnabled
        //settingsInteractor.updateThemeSettings(settings)

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

    }


}