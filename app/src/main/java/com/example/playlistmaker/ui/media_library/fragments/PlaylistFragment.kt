package com.example.playlistmaker.ui.media_library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.ui.main.fragment.BindingFragment

class PlaylistFragment : BindingFragment<FragmentPlaylistsBinding>() {

    companion object{
        fun newInstanse() = PlaylistFragment()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

}