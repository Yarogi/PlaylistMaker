package com.example.playlistmaker.di

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<PlayerViewModel> { (trackJson: String?) ->
        val gson: Gson = get()
        val track = gson.fromJson(trackJson, Track::class.java)
        PlayerViewModel(track = track, playerInteractor = get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(
            searchInteractor = get(),
            searchHistoryInteractor = get()
        )
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            application = androidApplication(),
            sharingInteractor = get(),
            settingsInterractor = get()
        )
    }

}