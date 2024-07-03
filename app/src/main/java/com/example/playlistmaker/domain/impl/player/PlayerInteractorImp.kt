package com.example.playlistmaker.domain.impl.player

import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.api.player.PlayerRepository
import com.example.playlistmaker.domain.model.PlaybackState
import com.example.playlistmaker.domain.model.Track

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

    override fun getPlaybackState(): PlaybackState {
        return player.getCurrentState()
    }

}