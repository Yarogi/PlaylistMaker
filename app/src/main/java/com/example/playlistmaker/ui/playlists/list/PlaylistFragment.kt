package com.example.playlistmaker.ui.playlists.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.ui.util.BindingFragment
import com.example.playlistmaker.presentation.playlists.list.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.presentation.playlists.list.PlaylistState
import com.example.playlistmaker.ui.playlists.item.PlaylistItemFragment

class PlaylistFragment : BindingFragment<FragmentPlaylistsBinding>() {

    companion object {
        fun newInstanse() = PlaylistFragment()
    }

    private val viewModel by viewModel<PlaylistViewModel>()
    private val adapter: PlaylistAdapter = PlaylistAdapter(
        object : PlaylistAdapter.Listener {
            override fun onPlaylistClick(playlist: Playlist) {
                findNavController().navigate(
                    R.id.action_libraryFragment_to_playlistItemFragment,
                    PlaylistItemFragment.createArgs(playlist = playlist)
                )
            }

        }
    )

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_playlistCreateFragment)
        }

        binding.playlists.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlists.adapter = adapter

        viewModel.playlistStateObserver().observe(viewLifecycleOwner) { state -> render(state) }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> renderContent(state.data)
            PlaylistState.Empty -> renderEmpty()
            PlaylistState.Loading -> renderLoading()
        }
    }

    private fun renderContent(data: List<Playlist>) {
        adapter.playlists.clear()
        adapter.playlists.addAll(data)
        adapter.notifyDataSetChanged()

        binding.playlists.isVisible = true
        binding.errorHolderGroup.isVisible = false


    }

    private fun renderLoading() {}

    private fun renderEmpty() {

        binding.playlists.isVisible = false
        binding.errorHolderGroup.isVisible = true

    }

}