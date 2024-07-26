package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.api.SettingsRepository
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings


class SettingsInteractorImpl(val repositoty: SettingsRepository) : SettingsInteractor {

    override fun getThemeSettings(): ThemeSettings {
        return repositoty.getThemeSettings()
    }

    override fun updateThemeSettings(settings: ThemeSettings) {
        repositoty.updateThemeSetting(settings)
    }

}