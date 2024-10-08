package com.example.playlistmaker.ui.media_library.playlists.edit

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.FragmentPlaylistEditBinding
import com.example.playlistmaker.presentation.media_library.playlists.edit_playlist.PlayListEditViewModel
import com.example.playlistmaker.presentation.media_library.playlists.edit_playlist.PlaylistEditState
import com.example.playlistmaker.domain.media_library.playlists.model.PlaylistCreateData
import com.example.playlistmaker.ui.util.pxToDP
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditFragment : Fragment() {

    private val viewModel by viewModel<PlayListEditViewModel>()

    private var _binding: FragmentPlaylistEditBinding? = null
    private val binding: FragmentPlaylistEditBinding get() = _binding!!

    private lateinit var nameTextWatcher: TextWatcher
    private lateinit var descriptionTextWatcher: TextWatcher

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

        initTextWatchers()

        viewModel.playListStateObserver().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistEditState.Content -> showPlaylistContent(state.data)
                PlaylistEditState.Empty -> showEmpty()
                PlaylistEditState.Loading -> {}
                is PlaylistEditState.Create -> {
                    Toast.makeText(
                        requireContext(),
                        "Плейлист ${state.data.name} создан",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                }
            }
        }

        //picker
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                viewModel.onCoverChanged(uri)
            }
        binding.playlistCover.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        binding.createButton.setOnClickListener {
            viewModel.savePlaylist()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyTextWatchers()
        _binding = null
    }

    private fun initTextWatchers() {

        nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onNameChanged(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        nameTextWatcher.let {
            binding.playlistName.addTextChangedListener(it)
        }

        descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onDescriptionChanged(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        descriptionTextWatcher.let {
            binding.playlistDescription.addTextChangedListener(it)
        }

    }

    private fun destroyTextWatchers() {
        nameTextWatcher?.let {
            binding.playlistName.removeTextChangedListener(it)
        }
        descriptionTextWatcher?.let {
            binding.playlistDescription.removeTextChangedListener(it)
        }
    }

    private fun showPlaylistContent(data: PlaylistCreateData) {

        if (binding.playlistName.text.toString() != data.name) {
            binding.playlistName.setText(data.name)
        }
        if (binding.playlistName.text.isNotEmpty()) {
            binding.playlistNameTitle.isVisible = true
            binding.playlistName.isSelected = true
            binding.createButton.isEnabled = true
        } else {
            binding.playlistNameTitle.isVisible = false
            binding.playlistName.isSelected = false
            binding.createButton.isEnabled = false
        }

        if (binding.playlistDescription.text.toString() != data.description) {
            binding.playlistDescription.setText(data.description)
        }
        if (binding.playlistDescription.text.isNotEmpty()) {
            binding.playlistDescriptionTitle.isVisible = true
            binding.playlistDescription.isSelected = true
        } else {
            binding.playlistDescriptionTitle.isVisible = false
            binding.playlistDescription.isSelected = false
        }

        showPlaylistCoverByUri(data.cover)

    }

    private fun showEmpty() {
        showPlaylistContent(PlaylistCreateData(name = "", description = "", cover = null))
    }

    private fun showPlaylistCoverByUri(uri: Uri?) {

        if (uri != null) {

            val imageView = binding.playlistCover
            val roudingRadius = pxToDP(imageView.context, 8);
            Glide.with(requireContext())
                .load(uri)
                .transform(CenterCrop(), RoundedCorners(roudingRadius))
                .into(imageView)
        }

    }

}