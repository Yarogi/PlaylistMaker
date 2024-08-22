package com.example.playlistmaker.presentation.settings

import com.example.playlistmaker.domain.settings.model.ThemeSettings

sealed interface SettingsState {
    data class Content(val settings: ThemeSettings) : SettingsState
}