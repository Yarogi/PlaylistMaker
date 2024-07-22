package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(val context: Context):ExternalNavigator {

    override fun shareLink(link: String) {

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, link)
        intent.setType("text/plain")
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)

    }

    override fun openLink(link: String) {



    }

    override fun openEmail(mailData: EmailData) {

    }

}