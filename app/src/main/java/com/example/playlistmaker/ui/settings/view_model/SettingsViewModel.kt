package com.example.playlistmaker.ui.settings.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.util.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.ui.settings.model.SettingsState

class SettingsViewModel(
    private val application: Application,
) : AndroidViewModel(application) {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    application = this[APPLICATION_KEY] as Application
                )
            }
        }
    }

    private val sharingInteractor by lazy {
        Creator.provideSharingInteractor(application)
    }
    private val settingsInterractor: SettingsInteractor by lazy {
        Creator.provideSettingsInteractor(application)
    }

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

        render(SettingsState.Content(currentSettings))

    }

    private fun render(state: SettingsState) {
        stateLiveData.postValue(state)
    }

}