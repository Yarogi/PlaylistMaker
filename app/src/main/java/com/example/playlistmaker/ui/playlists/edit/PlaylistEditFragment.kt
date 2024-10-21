package com.example.playlistmaker.ui.playlists.edit

import com.example.playlistmaker.presentation.playlists.edit.PlaylistEditViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditFragment:PlaylistCreateFragment() {

    private val viewModel by viewModel<PlaylistEditViewModel>()

}