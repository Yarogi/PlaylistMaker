package com.example.playlistmaker.creator

import com.example.playlistmaker.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.impl.PlayerInteractorImp

object Creator {

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImp(provideGetPlayerRepository())
    }

    private fun provideGetPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }


}