package com.example.playlistmaker.ui.media_library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentFeaturedTracksBinding
import com.example.playlistmaker.ui.main.fragment.BindingFragment

class FeaturedTracksFragment : BindingFragment<FragmentFeaturedTracksBinding>() {

    companion object{
        fun newInstanse() = FeaturedTracksFragment()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFeaturedTracksBinding {
        return FragmentFeaturedTracksBinding.inflate(inflater, container, false)
    }

}