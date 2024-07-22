package com.example.playlistmaker.ui.settings.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val application: Application,
    private val settingsInterractor: SettingsInteractor,
) : AndroidViewModel(application) {

    private val sharingInteractor by lazy {
        Creator.provideSharingInteractor(application)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    application = this[APPLICATION_KEY] as Application,
                    settingsInterractor = Creator.provideSettingsInteractor()
                )
            }
        }
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

}