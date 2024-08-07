package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.network.TrackSearchApi
import com.example.playlistmaker.data.search.storage.HistoryStorage
import com.example.playlistmaker.data.search.storage.impl.HistoryStorageImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.di.util.DINames
import com.example.playlistmaker.domain.sharing.api.ExternalNavigator
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    factory<MediaPlayer> { MediaPlayer() }

    factory { Gson() }

    single(named(name = DINames.history_pref)) {
        androidContext().getSharedPreferences(
            "playlistmaker_search_preferences",
            Context.MODE_PRIVATE
        )
    }

    single(named(name = DINames.settings_pref)) {
        androidContext().getSharedPreferences(
            "playlistmaker_settings_preferences",
            Context.MODE_PRIVATE
        )
    }

    single<HistoryStorage> {
        HistoryStorageImpl(get(named(name = DINames.history_pref)), get())
    }

    single<TrackSearchApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackSearchApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = androidContext())
    }


}