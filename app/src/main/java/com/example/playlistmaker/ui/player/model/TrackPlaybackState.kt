package com.example.playlistmaker.ui.player.model

sealed class TrackPlaybackState(val currentDuartion:Int) {

    object Loading: TrackPlaybackState(0)
    object Ready: TrackPlaybackState(0)
    class Paused(currentDuartion: Int): TrackPlaybackState(currentDuartion)
    class Played(currentDuartion: Int): TrackPlaybackState(currentDuartion)


}