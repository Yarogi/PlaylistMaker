package com.example.playlistmaker.di

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.presentation.media_player.view_model.FeaturedTracksViewModel
import com.example.playlistmaker.presentation.media_player.view_model.PlaylistViewModel
import com.example.playlistmaker.presentation.player.PlayerViewModel
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.presentation.settings.SettingsViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
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

    viewModel<FeaturedTracksViewModel> {
        FeaturedTracksViewModel()
    }

    viewModel<PlaylistViewModel> {
        PlaylistViewModel()
    }

}