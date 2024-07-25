package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.sharing.api.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.api.SharringResoursesStore
import com.example.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val resoursesStore: SharringResoursesStore,
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return resoursesStore.getShareAppLink()
    }

    private fun getSupportEmailData(): EmailData {
        return resoursesStore.getSupportEmailData()
    }

    private fun getTermLink(): String {
        return resoursesStore.getTermLink()
    }

}