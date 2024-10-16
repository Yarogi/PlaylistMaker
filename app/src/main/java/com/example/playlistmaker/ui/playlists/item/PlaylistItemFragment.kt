package com.example.playlistmaker.ui.playlists.item

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import com.example.playlistmaker.ui.player.PlayerFragment
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.ui.util.trackDurationToString
import com.example.playlistmaker.ui.util.tracksQuantityToString
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistItemFragment : Fragment() {

    companion object {

        private const val PLAYLIST_ID_KEY = "playlistId"
        fun createArgs(playlist: Playlist) = bundleOf(PLAYLIST_ID_KEY to playlist.id)

    }

    private var _binding: FragmentPlaylistItemBinding? = null
    private val binding: FragmentPlaylistItemBinding get() = _binding!!

    private val viewModel by viewModel<PlaylistItemViewModel>()

    private val adapter = PlaylistTrackAdapter(object : PlaylistTrackAdapter.Listener {
        override fun onClickTrackListener(track: Track) {
            findNavController().navigate(
                R.id.action_playlistItemFragment_to_playerFragment,
                PlayerFragment.createArgs(track = track)
            )
        }

        override fun onLongClickListener(track: Track) {
            confirmRemoveTrackDialog(track).show()
        }
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

        binding.backButton.setOnClickListener { findNavController().popBackStack() }

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

        showPlaylistCover(data.coverPathUri)

        binding.playlistName.text = data.name
        binding.playlistDescription.text = data.description
        binding.playlistDurationView.text = trackDurationToString(data.totalDuration)
        binding.playlistTracksQuantityView.text = tracksQuantityToString(data.tracksQuantity)

        updateTracklist(data.tracks)

    }

    private fun showPlaylistCover(coverUri: Uri?) {

        binding.coverHolder.isVisible = true
        binding.cover.isVisible = false

        //Cover
        binding.cover.isVisible = false
        coverUri?.let {
            Glide.with(requireContext())
                .load(coverUri)
                .transform(CenterCrop())
                .into(binding.cover)
                .let {
                    binding.cover.isVisible = true
                }
        }

    }

    private fun updateTracklist(tracks: List<Track>) {

        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()

    }

    private fun confirmRemoveTrackDialog(track: Track): MaterialAlertDialogBuilder {

        val dialogTitle = getString(R.string.delete_track)
        val dialogMessage = getString(R.string.question_sure_remove_track_from_playlist)
        val cancelTitle = getString(R.string.cancel)
        val finishTitle = getString(R.string.delete)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setNeutralButton(cancelTitle) { dialog, which -> }
            .setPositiveButton(finishTitle) { dialog, which -> viewModel.removeFromPlaylist(track) }
    }

}