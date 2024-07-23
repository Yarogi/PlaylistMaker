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
import com.example.playlistmaker.ui.player.model.PlayerState
import com.example.playlistmaker.ui.search.pxToDP
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
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

    //Player
    private lateinit var playTrackBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val json = intent.getStringExtra(CURRENT_TRACK_KEY)
        viewModel = ViewModelProvider(
            owner = this,
            factory = PlayerViewModel.getViewModelFactory(json)
        )[PlayerViewModel::class.java]


        viewModel.trackObserver().observe(this) { track ->
            fillTrackInformation(track)
        }
        viewModel.playerStateObserver().observe(this) { state ->
            renderState(state)
        }

        binding.panelBackArrow.setOnClickListener { finish() }

        playTrackBtn = binding.playTrack
        playTrackBtn.setOnClickListener {
            viewModel.changePlayState()
        }

        viewModel.preparePlayer()
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

    private fun renderState(state: PlayerState) {

        showDuration(state.currentDuartion)

        when (state) {

            PlayerState.Loading -> {
                playTrackBtn.isEnabled = false
            }

            is PlayerState.Paused -> {
                playTrackBtn.setImageResource(R.drawable.play_button)
            }

            is PlayerState.Played -> {
                playTrackBtn.setImageResource(R.drawable.pause_button)
            }

            PlayerState.Ready -> {
                playTrackBtn.isEnabled = true
                playTrackBtn.setImageResource(R.drawable.play_button)
            }

        }

    }

    private fun showDuration(playDuration: Int = 0) {
        binding.playTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playDuration)
    }

}