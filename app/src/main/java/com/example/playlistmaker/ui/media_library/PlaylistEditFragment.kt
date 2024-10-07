package com.example.playlistmaker.ui.media_library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentPlaylistEditBinding
import com.example.playlistmaker.presentation.media_library.playlists.PlayListEditViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditFragment : Fragment() {

    private val viewModel by viewModel<PlayListEditViewModel>()

    private var _binding: FragmentPlaylistEditBinding? = null
    private val binding: FragmentPlaylistEditBinding  get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaylistEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}