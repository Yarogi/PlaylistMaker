package com.example.playlistmaker.ui.featured

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFeaturedTracksBinding
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.presentation.featured.FeaturedTracksState
import com.example.playlistmaker.presentation.featured.FeaturedTracksViewModel
import com.example.playlistmaker.ui.player.PlayerFragment
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.ui.util.BindingFragment
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeaturedTracksFragment : BindingFragment<FragmentFeaturedTracksBinding>() {

    companion object {
        fun newInstanse(): FeaturedTracksFragment = FeaturedTracksFragment()
    }

    private val viewModel by viewModel<FeaturedTracksViewModel>()

    private val playTrackDebouncer: (Track) -> Unit by lazy {
        debounce(0L, lifecycleScope, false) { track ->
            findNavController().navigate(
                R.id.action_libraryFragment_to_playerFragment,
                PlayerFragment.createArgs(track)
            )
        }
    }
    private val trackListAdapter = TrackAdapter(object : TrackAdapter.Listener {
        override fun onClickTrackListener(track: Track) {
            playTrackDebouncer(track)
        }
    })

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFeaturedTracksBinding {
        return FragmentFeaturedTracksBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trackListView.adapter = trackListAdapter
        viewModel.stateObserver().observe(viewLifecycleOwner) { state -> render(state) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.readFeaturedTracks()
    }

    private fun render(state: FeaturedTracksState) {
        when (state) {
            is FeaturedTracksState.Content -> showTrackList(state.data)
            FeaturedTracksState.Empty -> showEmptyList()
            FeaturedTracksState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        setElementVisibile(progresBarIsVisible = true)
    }

    private fun showEmptyList() {
        setElementVisibile(holderIsVisible = true)
    }

    private fun showTrackList(trackList: List<Track>) {
        trackListAdapter.tracks.clear()
        trackListAdapter.tracks.addAll(trackList)
        trackListAdapter.notifyDataSetChanged()
        setElementVisibile(trackListIsVisible = true)
    }

    private fun setElementVisibile(
        trackListIsVisible: Boolean = false,
        progresBarIsVisible: Boolean = false,
        holderIsVisible: Boolean = false,
    ) {
        binding.holderEmpty.isVisible = holderIsVisible
        binding.trackListView.isVisible = trackListIsVisible
        binding.progressBar.isVisible = progresBarIsVisible

    }

}