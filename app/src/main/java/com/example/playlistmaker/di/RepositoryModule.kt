package com.example.playlistmaker.di

import com.example.playlistmaker.data.media_library.mapper.TrackDbMapper
import com.example.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.di.util.DINames
import com.example.playlistmaker.domain.player.api.PlayerRepository
import com.example.playlistmaker.domain.search.api.SearchHistoryRepository
import com.example.playlistmaker.domain.search.api.TracksRepository
import com.example.playlistmaker.domain.settings.api.SettingsRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    factory { TrackDbMapper() }

    factory<PlayerRepository> { PlayerRepositoryImpl(mediaPlayer = get()) }

    single<TracksRepository> {
        TracksRepositoryImpl(networkClient = get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(storage = get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(sharedPreferences = get(named(name = DINames.settings_pref)))
    }

}