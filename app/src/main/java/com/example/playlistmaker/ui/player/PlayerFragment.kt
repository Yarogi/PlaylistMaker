package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.media_library.playlists.model.Playlist
import com.example.playlistmaker.presentation.media_library.playlists.list.PlaylistState
import com.example.playlistmaker.presentation.player.PlayerFeaturedState
import com.example.playlistmaker.presentation.player.PlayerPlaylistState
import com.example.playlistmaker.presentation.player.PlayerViewModel
import com.example.playlistmaker.presentation.player.TrackPlaybackState
import com.example.playlistmaker.presentation.player.TrackScreenState
import com.example.playlistmaker.ui.player.playlists.PlayerPlaylistAdapter
import com.example.playlistmaker.ui.util.pxToDP
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {

    companion object {
        const val CURRENT_TRACK_KEY = "track"
        fun createArgs(track: Track): Bundle {
            val json = Gson().toJson(track)
            return bundleOf(CURRENT_TRACK_KEY to json)
        }
    }

    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding get() = _binding!!

    private var trackJson = ""
    private val viewModel by viewModel<PlayerViewModel> { parametersOf(trackJson) }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val adapter: PlayerPlaylistAdapter by lazy {
        PlayerPlaylistAdapter(
            object : PlayerPlaylistAdapter.Listener {
                override fun onClickListener(playlist: Playlist) {
                    viewModel.addTrackToPlaylist(playlist)
                }

            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackJson = requireArguments().getString(CURRENT_TRACK_KEY) ?: ""

        viewModel.trackScreenStateObserver().observe(viewLifecycleOwner) { state ->
            when (state) {
                is TrackScreenState.Content -> {
                    fillTrackInformation(state.track)
                }

                TrackScreenState.Loading -> {}
            }
        }

        viewModel.playerStateObserver().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        binding.panelBackArrow.setOnClickListener { findNavController().popBackStack() }
        binding.playTrack.setOnClickListener { viewModel.changePlayState() }

        viewModel.isFavoriteObserver().observe(viewLifecycleOwner) { state ->
            renderFeaturedState(state)
        }
        binding.isFavoriteButton.setOnClickListener {
            viewModel.isFavoriteOnClick()
        }

        //bottom_sheet
        binding.overlay.isVisible = false
        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                updateOverlayVisible(newState)
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED,
                    BottomSheetBehavior.STATE_EXPANDED,
                    -> viewModel.getAllPlaylists(newState)

                    BottomSheetBehavior.STATE_HIDDEN -> viewModel.clearPlaylists(newState)
                    else -> {}
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

        binding.addPlaylist.setOnClickListener {
            viewModel.getAllPlaylists(BottomSheetBehavior.STATE_COLLAPSED)
        }
        binding.playlistsRecyclerView.adapter = adapter
        viewModel.playlistStateObserver().observe(viewLifecycleOwner) { state ->
            renderPlaylistState(state)
        }
        binding.createNewPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_playlistEditFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer(сheckPlayback = true)
    }

    private fun fillTrackInformation(track: Track) {

        //Cover
        val coverImgView = binding.cover
        val artWorkRadius = pxToDP(coverImgView.context, 8)
        Glide.with(coverImgView)
            .load(track.getCoverArtwork())
            .centerCrop()
            .placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(artWorkRadius))
            .into(coverImgView)

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.duration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val hasCollectionInfo = !track.collectionName.isNullOrEmpty()
        binding.collectionNameGroup.isVisible = hasCollectionInfo
        if (hasCollectionInfo) {
            binding.album.text = track.collectionName
        }

        binding.releaseDate.text = track.getReleaseYear()
        binding.genre.text = track.primaryGenreName
        binding.country.text = track.country

    }

    private fun renderState(state: TrackPlaybackState) {

        changeProgress(state.progress)
        changeButtonStyle(state)

    }

    private fun changeButtonStyle(state: TrackPlaybackState) {
        when (state) {

            TrackPlaybackState.Loading -> {
                binding.playTrack.isEnabled = false
            }

            TrackPlaybackState.Ready -> {
                binding.playTrack.isEnabled = true
                binding.playTrack.setImageResource(R.drawable.play_button)
            }

            is TrackPlaybackState.Paused -> {
                binding.playTrack.setImageResource(R.drawable.play_button)
            }

            is TrackPlaybackState.Played -> {
                binding.playTrack.setImageResource(R.drawable.pause_button)
            }

        }
    }

    private fun renderFeaturedState(state: PlayerFeaturedState) {

        when (state) {
            is PlayerFeaturedState.Content -> {
                binding.isFavoriteButton.isSelected = state.isFeatured
                binding.isFavoriteButton.isEnabled = true
            }

            PlayerFeaturedState.Loading -> binding.isFavoriteButton.isEnabled = false
        }

    }

    private fun changeProgress(progress: Int) {

        val progressText = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(progress)

        if (binding.playTime.text != progressText) {
            binding.playTime.text = progressText
        }

    }


    private fun renderPlaylistState(state: PlayerPlaylistState) {
        if (bottomSheetBehavior.state != state.listState) {
            bottomSheetBehavior.state = state.listState
            updateOverlayVisible(state.listState)
        }

        when (state) {
            is PlayerPlaylistState.Content -> updatePlaylistsData(state.data)
            is PlayerPlaylistState.Loading -> {}
        }

    }

    private fun updatePlaylistsData(playlists: List<Playlist>) {
        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()
    }

    private fun updateOverlayVisible(listState: Int) {
        binding.overlay.isVisible = listState != BottomSheetBehavior.STATE_HIDDEN
    }


}