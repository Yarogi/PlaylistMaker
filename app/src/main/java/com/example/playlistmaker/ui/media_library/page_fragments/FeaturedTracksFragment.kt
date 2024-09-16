package com.example.playlistmaker.ui.media_library.page_fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentFeaturedTracksBinding
import com.example.playlistmaker.ui.util.BindingFragment
import com.example.playlistmaker.presentation.media_library.FeaturedTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeaturedTracksFragment : BindingFragment<FragmentFeaturedTracksBinding>() {

    companion object{
        fun newInstanse() = FeaturedTracksFragment()
    }

    private val viewModel by viewModel<FeaturedTracksViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFeaturedTracksBinding {
        return FragmentFeaturedTracksBinding.inflate(inflater, container, false)
    }

}