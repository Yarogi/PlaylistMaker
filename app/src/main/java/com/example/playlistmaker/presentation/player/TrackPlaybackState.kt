package com.example.playlistmaker.presentation.player

sealed class TrackPlaybackState(val progress:Int) {

    object Loading: TrackPlaybackState(0)
    object Ready: TrackPlaybackState(0)
    class Paused(progress: Int): TrackPlaybackState(progress)
    class Played(progress: Int): TrackPlaybackState(progress)


}