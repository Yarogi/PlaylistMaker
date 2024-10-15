package com.example.playlistmaker.ui.playlists.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistItemBinding
import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.presentation.playlists.item.PlaylistItemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistItemFragment : Fragment() {

    companion object {

        private const val PLAYLIST_ID_KEY = "playlistId"
        fun createArgs(playlist: Playlist) = bundleOf(PLAYLIST_ID_KEY to playlist.id)

    }

    private var _binding: FragmentPlaylistItemBinding? = null
    private val binding: FragmentPlaylistItemBinding get() = _binding!!

    private val viewModel by viewModel<PlaylistItemViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaylistItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}