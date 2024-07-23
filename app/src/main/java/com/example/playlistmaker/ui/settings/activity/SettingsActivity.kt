package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.model.SettingsState
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    private lateinit var viewModel: SettingsViewModel
    private lateinit var themeSwitcher: SwitchMaterial

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
        themeSwitcher = binding.themeSwitcher
        themeSwitcher.setOnCheckedChangeListener { switcher, cheked ->
            viewModel.switchMode(cheked)
        }

    }

    private fun renderState(state: SettingsState) {

        when (state) {
            is SettingsState.Content -> {
                val darkModeEnable = state.settings.darkMode
                if (themeSwitcher.isChecked != darkModeEnable) {
                    themeSwitcher.isChecked = darkModeEnable
                }

            }
        }

    }

}