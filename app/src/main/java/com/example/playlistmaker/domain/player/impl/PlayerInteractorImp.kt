package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.api.PlayerInteractor
import com.example.playlistmaker.domain.player.api.PlayerRepository
import com.example.playlistmaker.domain.player.model.PlaybackStatus
import com.example.playlistmaker.domain.main.model.Track

class PlayerInteractorImp(override val player: PlayerRepository) : PlayerInteractor {

    override fun prepared(
        track: Track,
        listener: PlayerInteractor.PrepareListener
    ) {
        player.prepared(track, listener)
    }

    override fun played() {
        player.played()
    }

    override fun paused() {
        player.paused()
    }

    override fun release() {
        player.release()
    }

    override fun getCurrentPosition(): Int {
        return player.getCurrentPosition()
    }

    override fun getPlaybackState(): PlaybackStatus {
        return player.getCurrentState()
    }

}