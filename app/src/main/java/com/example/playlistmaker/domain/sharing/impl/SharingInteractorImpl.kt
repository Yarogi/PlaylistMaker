package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink():String{
        return "https://practicum.yandex.ru/android-developer"
    }

    private fun getSupportEmailData():EmailData{
        return EmailData(
            recipient = "yachmenyov.igor@yandex.ru",
            theme = "Сообщение разработчикам и разработчицам приложения Playlist Maker",
            body = "Спасибо разработчикам и разработчицам за крутое приложение!"
        )
    }

    private fun getTermLink():String{
        return "https://yandex.ru/legal/practicum_offer"
    }

}