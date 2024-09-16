package com.example.playlistmaker.di

import com.example.playlistmaker.domain.player.api.PlayerInteractor
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.search.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import java.util.concurrent.Executors
import org.koin.dsl.module

val interactorModule = module {

    factory { Executors.newCachedThreadPool() }

    //Player

    factory<PlayerInteractor> {
        PlayerInteractorImpl(player = get(), libraryRepository = get())
    }

    //Search

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(repository = get())
    }

    single<TracksInteractor> {
        TracksInteractorImpl(
            repository = get()
        )
    }

    //Settings

    single<SettingsInteractor>{
        SettingsInteractorImpl(repositoty = get())
    }

    //Sharring

    single<SharingInteractor>{
        SharingInteractorImpl(externalNavigator = get(), resoursesStore = get())
    }

}