package com.example.playlistmaker.di

import com.example.playlistmaker.domain.sharing.api.SharringResoursesStore
import com.example.playlistmaker.presentation.sharing.SharringResoursesStoreImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val presenterModule = module {
    single<SharringResoursesStore>{
        SharringResoursesStoreImpl(androidContext())
    }
}