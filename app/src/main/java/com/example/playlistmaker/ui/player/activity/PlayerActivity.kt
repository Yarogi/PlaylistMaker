package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.ui.player.model.TrackPlaybackState
import com.example.playlistmaker.ui.player.model.TrackScreenState
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.pxToDP
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    companion object {
        const val CURRENT_TRACK_KEY = "track"
    }

    private lateinit var viewModel: PlayerViewModel
    val binding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val json = intent.getStringExtra(CURRENT_TRACK_KEY)
        viewModel = ViewModelProvider(
            owner = this,
            factory = PlayerViewModel.getViewModelFactory(json)
        )[PlayerViewModel::class.java]


        viewModel.trackScreenStateObserver().observe(this) { state ->
            when (state) {
                is TrackScreenState.Content -> fillTrackInformation(state.track)
                TrackScreenState.Loading -> {}
            }
        }

        viewModel.playerStateObserver().observe(this) { state ->
            renderState(state)
        }

        binding.panelBackArrow.setOnClickListener { finish() }

        binding.playTrack.setOnClickListener {viewModel.changePlayState()}
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
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

        changeButtonStyle(state)

        binding.playTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(state.currentDuartion)

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

}