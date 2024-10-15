package com.example.playlistmaker.ui.playlists.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistItemBinding
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.playlists.model.Playlist
import com.example.playlistmaker.presentation.playlists.item.PlaylistItemState
import com.example.playlistmaker.presentation.playlists.item.PlaylistItemViewModel
import com.example.playlistmaker.presentation.playlists.item.model.PlaylistDetailedInfo
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.ui.util.trackDurationToString
import com.example.playlistmaker.ui.util.tracksQuantityToString
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistItemFragment : Fragment() {

    companion object {

        private const val PLAYLIST_ID_KEY = "playlistId"
        fun createArgs(playlist: Playlist) = bundleOf(PLAYLIST_ID_KEY to playlist.id)

    }

    private var _binding: FragmentPlaylistItemBinding? = null
    private val binding: FragmentPlaylistItemBinding get() = _binding!!

    private val viewModel by viewModel<PlaylistItemViewModel>()

    private val adapter = TrackAdapter(object : TrackAdapter.Listener {
        override fun onClickTrackListener(track: Track) {}
    })

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

        viewModel.stateLiveDataObserver().observe(viewLifecycleOwner) { render(it) }
        binding.trackListView.adapter = adapter

        viewModel.updatePlaylistInfoById(requireArguments().getInt(PLAYLIST_ID_KEY))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun render(state: PlaylistItemState) {

        when (state) {
            is PlaylistItemState.Content -> showPlaylistDetales(state.data)
            PlaylistItemState.Loading -> {}
        }

    }

    private fun showPlaylistDetales(data: PlaylistDetailedInfo) {

        //Cover
        binding.cover.isVisible = false
        data.coverPathUri?.let {
            Glide.with(requireContext())
                .load(data.coverPathUri)
                .transform(CenterCrop())
                .into(binding.cover).let {
                    binding.cover.isVisible = true
                }
        }

        binding.playlistName.text = data.name
        binding.playlistDescription.text = data.description
        binding.playlistDurationView.text = trackDurationToString(data.totalDuration)
        binding.playlistTracksQuantityView.text = tracksQuantityToString(data.tracksQuantity)

        updateTracklist(data.tracks)

    }

    private fun updateTracklist(tracks: List<Track>) {

        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()

    }


}