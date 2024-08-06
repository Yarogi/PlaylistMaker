package com.example.playlistmaker.ui.settings.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.ui.settings.model.SettingsState
import com.example.playlistmaker.util.App

class SettingsViewModel(
    private val application: Application,
    private val sharingInteractor: SharingInteractor,
    private val settingsInterractor: SettingsInteractor,
) : AndroidViewModel(application) {


    private val currentSettings by lazy { settingsInterractor.getThemeSettings() }

    private val stateLiveData =
        MutableLiveData<SettingsState>(
            SettingsState.Content(currentSettings)
        )

    fun observerStateLiveData(): LiveData<SettingsState> = stateLiveData

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun switchMode(darkModeEnable: Boolean) {

        if (currentSettings.darkMode == darkModeEnable) return

        this.currentSettings.darkMode = darkModeEnable
        (application as App).switchTheme(currentSettings)

        settingsInterractor.updateThemeSettings(currentSettings)

        render(SettingsState.Content(currentSettings))

    }

    private fun render(state: SettingsState) {
        stateLiveData.postValue(state)
    }

}