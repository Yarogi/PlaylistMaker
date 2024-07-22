package com.example.playlistmaker.ui.settings.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Exit
        binding.exitBtn.setOnClickListener {
            this.finish()
        }

        viewModel = ViewModelProvider(
            owner = this,
            factory = SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]


        //Shared
        binding.shareApp.setOnClickListener {
            viewModel.shareApp()
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
        themeSwitcher.isChecked = (applicationContext as App).settings.darkMode
        themeSwitcher.setOnCheckedChangeListener { switcher, cheked ->
            (applicationContext as App).switchTheme(cheked)
        }

    }
}