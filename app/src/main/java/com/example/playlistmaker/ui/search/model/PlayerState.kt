package com.example.playlistmaker.ui.search.model

sealed class PlayerState(val currentDuartion:Int) {

    object Loading:PlayerState(0)
    object Ready:PlayerState(0)
    class Paused(currentDuartion: Int): PlayerState(currentDuartion)
    class Played(currentDuartion: Int): PlayerState(currentDuartion)


}