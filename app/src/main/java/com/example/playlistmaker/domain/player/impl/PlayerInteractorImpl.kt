package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.media_library.favorites.api.FeaturedTracksRepository
import com.example.playlistmaker.domain.player.api.PlayerInteractor
import com.example.playlistmaker.domain.player.api.PlayerRepository
import com.example.playlistmaker.domain.player.model.PlaybackStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PlayerInteractorImpl(
    override val player: PlayerRepository,
    val libraryRepository: FeaturedTracksRepository,
) : PlayerInteractor {

    override fun prepared(
        track: Track,
        listener: PlayerInteractor.PrepareListener,
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

    override fun getCurrentProgress(): Int {
        return player.getCurrentProgress()
    }

    override fun getPlaybackState(): PlaybackStatus {
        return player.getCurrentState()
    }

    override fun addToLibrary(track: Track) {
        libraryRepository.addTrack(track = track)
    }

    override fun removeFromLibrary(track: Track) {
        libraryRepository.removeTrack(track = track)
    }

    override suspend fun trackInFavorite(track: Track): Flow<Boolean> = flow {

        val foundTrack = libraryRepository.getTrackById(id = track.trackId)
        emit(foundTrack != null)

    }.flowOn(Dispatchers.IO)


}