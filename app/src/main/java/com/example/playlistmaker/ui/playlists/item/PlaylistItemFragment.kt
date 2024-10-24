package com.example.playlistmaker.ui.playlists.item

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource
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
import org.koin.core.component.getScopeId

class PlaylistItemFragment : Fragment() {

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

        binding.rvTracklist.adapter = adapter
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivShare.setOnClickListener { viewModel.sharePlaylist() }

        binding.ivMenu.setOnClickListener { viewModel.getCommandsList() }

        initBottomSheetBehavor()
        initMenuViews(playlistId)

        viewModel.updatePlaylistInfoById(playlistId)

        viewModel.stateLiveDataObserver().observe(viewLifecycleOwner) { render(it) }
        viewModel.shareStateLiveDataObserver().observe(viewLifecycleOwner) { renderShareState(it) }

    }

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBottomSheetBehavor() {

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bsbTracklist)
            .apply { state = BottomSheetBehavior.STATE_COLLAPSED }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

    }

    private fun initMenuViews(playlistId: Int) {

        menuSheetBehavior = BottomSheetBehavior.from(binding.bsbMenu)
            .apply { state = BottomSheetBehavior.STATE_HIDDEN }
        menuSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                setPlaylistVisible(newState != BottomSheetBehavior.STATE_COLLAPSED)
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    viewModel.updatePlaylistInfoByLast()
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        //buttons
        binding.btnMenuShare.setOnClickListener {
            viewModel.updatePlaylistInfoByLast()
            viewModel.sharePlaylist()
        }
        binding.btnMenuEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistItemFragment_to_playlistEditFragment,
                PlaylistEditFragment.createArgs(playlistId = playlistId)
            )
        }
        binding.btnMenuDelete.setOnClickListener {
            confirmDeleteDialog().show()
            viewModel.updatePlaylistInfoByLast()
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
        binding.tvPlaylistName.text = data.name
        binding.tvPlaylistDescription.isVisible = data.description.isNotEmpty()
        binding.tvPlaylistDescription.text = data.description
        binding.tvPlaylistDuration.text =
            trackDurationToString(requireContext(), data.totalDuration)
        binding.tvPlaylistTracksQuantity.text =
            tracksQuantityToString(requireContext(), data.tracksQuantity)
    }

    private fun showComandsPanel(data: PlaylistDetailedInfo) {

        setCommandMenuVisible(isVisible = true)
        showPlaylistCover(data.coverPathUri)
        updateDescriptioView(data)

        binding.itemSecondary.name.text = data.name
        binding.itemSecondary.tracksQuantity.text = tracksQuantityToString(
            requireContext(), data.tracksQuantity
        )

        Glide.with(requireContext())
            .load(data.coverPathUri)
            .placeholder(R.drawable.track_placeholder)
            .transform(CenterCrop(), RoundedCorners(pxToDP(requireContext(), 2)))
            .into(binding.itemSecondary.cover)

        setPlaylistVisible(isVisible = false)

    }

    private fun setCommandMenuVisible(isVisible: Boolean) {
        binding.overlay.isVisible = isVisible

        menuSheetBehavior.state =
            if (isVisible) BottomSheetBehavior.STATE_COLLAPSED
            else BottomSheetBehavior.STATE_HIDDEN

    }

    private fun showPlaylistCover(coverUri: Uri?) {

        binding.ivCoverHolder.isVisible = true
        binding.ivCoverPrimary.isVisible = false

        //Cover
        binding.ivCoverPrimary.isVisible = false
        coverUri?.let {
            Glide.with(requireContext())
                .load(coverUri)
                .transform(CenterCrop())
                .into(binding.ivCoverPrimary)
                .let {
                    binding.ivCoverPrimary.isVisible = true
                }
        }

    }

    private fun setPlaylistVisible(isVisible: Boolean) {
        if (binding.bsbTracklist.isVisible != isVisible) {
            binding.bsbTracklist.isVisible = isVisible
        }
    }

    private fun updateTracklist(tracks: List<Track>) {

        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()

        binding.rvTracklist.isVisible = tracks.isNotEmpty()
        binding.llEmptyTracklistHolder.isVisible = tracks.isEmpty()
    }

    private fun confirmRemoveTrackDialog(track: Track): MaterialAlertDialogBuilder {

        val dialogMessage = getString(R.string.question_sure_remove_track_from_playlist)
        val cancelTitle = getString(R.string.no).uppercase()
        val finishTitle = getString(R.string.yes).uppercase()

        return MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setMessage(dialogMessage)
            .setNegativeButton(cancelTitle) { dialog, which -> }
            .setPositiveButton(finishTitle) { dialog, which -> viewModel.removeFromPlaylist(track) }
    }

    private fun confirmDeleteDialog(): MaterialAlertDialogBuilder {

        return MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setMessage(
                getString(R.string.question_sure_delete_playlist).replace(
                    "%1", binding.tvPlaylistName.text.toString()
                )
            )
            .setNegativeButton(getString(R.string.no).uppercase()) { dialog, which -> }
            .setPositiveButton(getString(R.string.yes).uppercase()) { dialog, which ->
                viewModel.deletePlaylist()
            }

    }

    companion object {

        private const val PLAYLIST_ID_KEY = "playlistId"
        fun createArgs(playlist: Playlist) = bundleOf(PLAYLIST_ID_KEY to playlist.id)

    }

}