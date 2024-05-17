package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //Exit
        val exitButton = findViewById<Button>(R.id.exitBtn)
        exitButton.setOnClickListener {
            this.finish()
        }

        //Shared
        val shareAppButton = findViewById<Button>(R.id.shareApp)
        shareAppButton.setOnClickListener {
            val message = getString(R.string.praktikum_andoid_dev_url)

            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.setType("text/plain")
            startActivity(intent)
        }

        //Write to support
        val writeToSupportButton = findViewById<Button>(R.id.writeToSupport)
        writeToSupportButton.setOnClickListener {

            val mailTheme = getString(R.string.write_to_support_theme)
            val message = getString(R.string.write_to_support_message)
            val supportEmail = getString(R.string.support_email)

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmail))
            intent.putExtra(Intent.EXTRA_SUBJECT, mailTheme)
            intent.putExtra(Intent.EXTRA_TEXT, message)

            startActivity(intent)

        }

        //Terms of use
        val termsOfUseButton = findViewById<Button>(R.id.termsOfUse)
        termsOfUseButton.setOnClickListener {
            val url = getString(R.string.praktikum_andoid_dev_term_of_use)
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )

            startActivity(intent)
        }

        //Night mode
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = (applicationContext as App).darkTheme
        themeSwitcher.setOnCheckedChangeListener { switcher, cheked ->
            (applicationContext as App).switchTheme(cheked)
        }

    }
}