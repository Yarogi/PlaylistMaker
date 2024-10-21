package com.example.playlistmaker.ui.playlists.item

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
import com.example.playlistmaker.presentation.playlists.item.PlaylistItemShareState
import com.example.playlistmaker.presentation.playlists.item.PlaylistItemState
import com.example.playlistmaker.presentation.playlists.item.PlaylistItemViewModel
import com.example.playlistmaker.presentation.playlists.item.model.PlaylistDetailedInfo
import com.example.playlistmaker.ui.player.PlayerFragment
import com.example.playlistmaker.ui.playlists.edit.PlaylistCreateFragment
import com.example.playlistmaker.ui.playlists.edit.PlaylistEditFragment
import com.example.playlistmaker.ui.util.pxToDP
import com.example.playlistmaker.ui.util.trackDurationToString
import com.example.playlistmaker.ui.util.tracksQuantityToString
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var menuSheetBehavior: BottomSheetBehavior<LinearLayout>

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

        val playlistId = requireArguments().getInt(PLAYLIST_ID_KEY)

        binding.trackListView.adapter = adapter
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
        binding.shareButton.setOnClickListener { viewModel.sharePlaylist() }

        binding.menuButton.setOnClickListener { viewModel.getCommandsList() }

        initBottomSheetBehavor()
        initMenuViews(playlistId)

        viewModel.updatePlaylistInfoById(playlistId)

        viewModel.stateLiveDataObserver().observe(viewLifecycleOwner) { render(it) }
        viewModel.shareStateLiveDataObserver().observe(viewLifecycleOwner) { renderShareState(it) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBottomSheetBehavor() {

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetBehavior)
            .apply { state = BottomSheetBehavior.STATE_COLLAPSED }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

    }

    private fun initMenuViews(playlistId: Int) {

        menuSheetBehavior = BottomSheetBehavior.from(binding.menuSheetBehavior)
            .apply { state = BottomSheetBehavior.STATE_HIDDEN }
        menuSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    viewModel.updatePlaylistInfoByLast()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        //buttons
        binding.menuShareButton.setOnClickListener { viewModel.sharePlaylist() }
        binding.menuEditButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistItemFragment_to_playlistEditFragment,
                PlaylistEditFragment.createArgs(playlistId = playlistId)
            )
        }
        binding.menuDeleteButton.setOnClickListener {
            confirmDeleteDialog().show()
        }

    }

    private fun render(state: PlaylistItemState) {

        when (state) {
            PlaylistItemState.Loading -> {}
            is PlaylistItemState.Content -> showPlaylistDetales(state.data)
            is PlaylistItemState.Commands -> showComandsPanel(state.data)
            PlaylistItemState.Deleted -> {
                findNavController().popBackStack()
            }
        }

    }

    private fun renderShareState(state: PlaylistItemShareState) {
        when (state) {
            PlaylistItemShareState.Empty -> {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.no_tracklist_in_playlist_shared),
                    Toast.LENGTH_SHORT
                )
                    .show()

            }

            PlaylistItemShareState.None -> {}
        }

    }

    private fun showPlaylistDetales(data: PlaylistDetailedInfo) {

        showPlaylistCover(data.coverPathUri)

        updateDescriptioView(data)

        setCommandMenuVisible(isVisible = false)

        updateTracklist(data.tracks)

    }

    private fun updateDescriptioView(data: PlaylistDetailedInfo) {
        binding.playlistName.text = data.name
        binding.playlistDescription.text = data.description
        binding.playlistDurationView.text = trackDurationToString(data.totalDuration)
        binding.playlistTracksQuantityView.text = tracksQuantityToString(data.tracksQuantity)
    }

    private fun showComandsPanel(data: PlaylistDetailedInfo) {

        setCommandMenuVisible(isVisible = true)
        showPlaylistCover(data.coverPathUri)
        updateDescriptioView(data)

        binding.itemSecondary.name.text = data.name
        binding.itemSecondary.tracksQuantity.text = tracksQuantityToString(data.tracksQuantity)

        Glide.with(requireContext())
            .load(data.coverPathUri)
            .placeholder(R.drawable.track_placeholder)
            .transform(CenterCrop(), RoundedCorners(pxToDP(requireContext(), 2)))
            .into(binding.itemSecondary.cover)
    }

    private fun setCommandMenuVisible(isVisible: Boolean) {
        binding.overlay.isVisible = isVisible
        menuSheetBehavior.state =
            if (isVisible) BottomSheetBehavior.STATE_COLLAPSED
            else BottomSheetBehavior.STATE_HIDDEN
    }

    private fun showPlaylistCover(coverUri: Uri?) {

        binding.coverHolder.isVisible = true
        binding.coverPrimary.isVisible = false

        //Cover
        binding.coverPrimary.isVisible = false
        coverUri?.let {
            Glide.with(requireContext())
                .load(coverUri)
                .transform(CenterCrop())
                .into(binding.coverPrimary)
                .let {
                    binding.coverPrimary.isVisible = true
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

    private fun confirmDeleteDialog(): MaterialAlertDialogBuilder {

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_playlist))
            .setMessage(
                getString(R.string.question_sure_delete_playlist).replace(
                    "%1", binding.playlistName.text.toString()
                )
            )
            .setNegativeButton(getString(R.string.no)) { dialog, which -> }
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                viewModel.deletePlaylist()
            }

    }

}