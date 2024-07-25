package com.example.playlistmaker.ui.settings.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.api.SharringResoursesStore
import com.example.playlistmaker.domain.sharing.model.EmailData

class SharringResoursesStoreImpl(val context: Context) : SharringResoursesStore {

    override fun getSupportEmailData(): EmailData {
        return EmailData(
            recipient = context.getString(R.string.support_email),
            theme = context.getString(R.string.write_to_support_theme),
            body = context.getString(R.string.write_to_support_message)
        )
    }

    override fun getTermLink(): String {
        return context.getString(R.string.praktikum_andoid_dev_term_of_use)
    }

    override fun getShareAppLink(): String {
        return context.getString(R.string.praktikum_andoid_dev_url)
    }
}