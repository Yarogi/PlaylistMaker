package com.example.playlistmaker.domain.sharing.api

import com.example.playlistmaker.domain.sharing.model.EmailData

interface SharringResoursesStore {
    fun getSupportEmailData(): EmailData
    fun getTermLink(): String
    fun getShareAppLink(): String

}