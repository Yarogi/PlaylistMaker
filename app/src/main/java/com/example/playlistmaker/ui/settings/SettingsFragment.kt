package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.presentation.settings.SettingsState
import com.example.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observerStateLiveData()
            .observe(viewLifecycleOwner) { state -> renderState(state) }

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