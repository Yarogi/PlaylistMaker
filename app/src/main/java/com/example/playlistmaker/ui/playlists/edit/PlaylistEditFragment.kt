package com.example.playlistmaker.ui.playlists.edit

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.presentation.playlists.edit.PlaylistEditViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditFragment : PlaylistCreateFragment() {

    override val viewModel by viewModel<PlaylistEditViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createButton.text = getString(R.string.save)
        binding.fragmentTitle.text = getString(R.string.edit)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        onBackCallBack.isEnabled = false
        viewModel.fillByPlaylistId(requireArguments().getInt(PLAYLIST_KEY))

    }

    override fun showCreate(newPlaylist: Playlist) {
        closeFragment()
    }

    companion object {
        private const val PLAYLIST_KEY = "playlistKey"
        fun createArgs(playlistId: Int) = bundleOf(PLAYLIST_KEY to playlistId)
    }

}