package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.model.SettingsState
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Exit
        binding.exitBtn.setOnClickListener {
            this.finish()
        }

        viewModel.observerStateLiveData().observe(this) { state -> renderState(state) }

        //Shared
        binding.shareApp.setOnClickListener {
            viewModel.shareApp()
        }
        //Write to support
        binding.writeToSupport.setOnClickListener {
            viewModel.openSupport()
        }
        //Terms of use
        binding.termsOfUse.setOnClickListener {
            viewModel.openTerms()
        }

        //Settings
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, cheked ->
            viewModel.switchMode(cheked)
        }

    }

    private fun renderState(state: SettingsState) {

        when (state) {
            is SettingsState.Content -> {
                val darkModeEnable = state.settings.darkMode
                if (binding.themeSwitcher.isChecked != darkModeEnable) {
                    binding.themeSwitcher.isChecked = darkModeEnable
                }

            }
        }

    }

}