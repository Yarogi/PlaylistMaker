package com.example.playlistmaker.di

import com.example.playlistmaker.domain.featured.api.FeaturedTracksInteractor
import com.example.playlistmaker.domain.featured.impl.FeaturedTracksInteractorImpl
import com.example.playlistmaker.domain.playlists.api.PlaylistEditInteractor
import com.example.playlistmaker.domain.playlists.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlists.impl.PlaylistEditInteractorImpl
import com.example.playlistmaker.domain.playlists.impl.PlaylistInteractorImpl
import com.example.playlistmaker.domain.player.api.PlayerInteractor
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.playlists.api.PlaylistItemInteractor
import com.example.playlistmaker.domain.playlists.impl.PlaylistItemInteractorImpl
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module
import java.util.concurrent.Executors

val interactorModule = module {

    factory { Executors.newCachedThreadPool() }

    //Player

    factory<PlayerInteractor> {
        PlayerInteractorImpl(player = get(), libraryRepository = get(), playlistRepository = get())
    }

    //Search

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(repository = get())
    }

    single<TracksInteractor> {
        TracksInteractorImpl(
            repository = get(),
        )
    }

    //Settings

    single<SettingsInteractor> {
        SettingsInteractorImpl(repositoty = get())
    }

    //Sharring

    single<SharingInteractor> {
        SharingInteractorImpl(externalNavigator = get(), resoursesStore = get())
    }

    //Featured_tracks
    single<FeaturedTracksInteractor> {
        FeaturedTracksInteractorImpl(featuredRepository = get())
    }

    //Playlists

    factory<PlaylistEditInteractor> {
        PlaylistEditInteractorImpl(repository = get())
    }
    single<PlaylistInteractor> { PlaylistInteractorImpl(repository = get()) }

    factory<PlaylistItemInteractor> {
        PlaylistItemInteractorImpl(repository = get())
    }

}