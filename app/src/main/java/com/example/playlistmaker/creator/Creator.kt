package com.example.playlistmaker.creator

import com.example.playlistmaker.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.domain.api.PlayerRepository

object Creator {

    fun provideGetPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

}