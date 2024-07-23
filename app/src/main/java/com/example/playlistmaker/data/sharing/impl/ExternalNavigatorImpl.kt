package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
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

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(link)
        )

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)

    }

    override fun openEmail(mailData: EmailData) {

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailData.recipient))
        intent.putExtra(Intent.EXTRA_SUBJECT, mailData.theme)
        intent.putExtra(Intent.EXTRA_TEXT, mailData.body)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)

    }

}