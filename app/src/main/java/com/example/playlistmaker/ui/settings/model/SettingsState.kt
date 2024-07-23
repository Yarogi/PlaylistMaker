package com.example.playlistmaker.ui.settings.model

import com.example.playlistmaker.domain.settings.model.ThemeSettings

sealed interface SettingsState {
    data class Content(val settings: ThemeSettings) : SettingsState
}