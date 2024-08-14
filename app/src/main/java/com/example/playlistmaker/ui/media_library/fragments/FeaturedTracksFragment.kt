package com.example.playlistmaker.ui.media_library.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentFeaturedTracksBinding
import com.example.playlistmaker.ui.main.fragment.BindingFragment
import com.example.playlistmaker.ui.media_library.view_model.FeaturedTracksViewModel
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