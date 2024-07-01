package com.example.playlistmaker.creator

import com.example.playlistmaker.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.api.player.PlayerRepository
import com.example.playlistmaker.domain.api.search.TracksInteractor
import com.example.playlistmaker.domain.api.search.TracksRepository
import com.example.playlistmaker.domain.impl.player.PlayerInteractorImp
import com.example.playlistmaker.domain.impl.search.TracksInteractorImpl

object Creator {

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImp(getPlayerRepository())
    }

    fun provideTracksInteractor():TracksInteractor{
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    private fun getTracksRepository(): TracksRepository{
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }


}