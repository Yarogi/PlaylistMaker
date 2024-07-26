package com.example.playlistmaker.domain.sharing.api

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {

    fun shareLink(link: String)
    fun openLink(link: String)
    fun openEmail(mailData: EmailData)

}