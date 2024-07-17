package com.example.playlistmaker.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.api.player.PlayerInteractor
import com.example.playlistmaker.domain.model.PlaybackState
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.search.pxToDP
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    lateinit var track: Track

    //Player
    private lateinit var playTrackBtn: ImageButton
    private lateinit var playTimeView: TextView

    private val playerInteractor by lazy { Creator.providePlayerInteractor() }

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    private val showDurationRunnable by lazy {
        object : Runnable {
            override fun run() {
                showCurrentDuration()
                handler.postDelayed(this, DURATION_DELAY)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backBtn = findViewById<ImageButton>(R.id.panelBackArrow)
        backBtn.setOnClickListener { finish() }

        val json = intent.getStringExtra(CURRENT_TRACK_KEY)
        track = Gson().fromJson(json, Track::class.java)

        //initializing form views
        fill()
        //prepare
        preparePlayer()

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.release()
    }

    private fun fill() {

        //Views: Cover image
        val coverImgView = findViewById<ImageView>(R.id.cover)

        //Views: Track title
        val trackNameView = findViewById<TextView>(R.id.trackName)
        val artistNameView = findViewById<TextView>(R.id.artistName)

        //Views: Controls
        playTrackBtn = findViewById(R.id.playTrack)
        playTrackBtn.isEnabled = false
        playTrackBtn.setOnClickListener { playbacklControl() }
        val addPlaylistBtn = findViewById<ImageButton>(R.id.addPlaylist)
        val addFavoriteBtn = findViewById<ImageButton>(R.id.like)

        //View: Play duration
        playTimeView = findViewById(R.id.playTime)

        //Views: Descriptions
        val durationView = findViewById<TextView>(R.id.duration)
        val collectionNameGroupView = findViewById<Group>(R.id.collectionNameGroup)
        val albumView = findViewById<TextView>(R.id.album)
        val releaseDateView = findViewById<TextView>(R.id.releaseDate)
        val genreView = findViewById<TextView>(R.id.genre)
        val countryView = findViewById<TextView>(R.id.country)

        //Cover image
        val artWorkRadius = pxToDP(coverImgView.context, 8)
        Glide.with(coverImgView)
            .load(track.getCoverArtwork())
            .centerCrop()
            .placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(artWorkRadius))
            .into(coverImgView)

        //Track title
        trackNameView.text = track.trackName
        artistNameView.text = track.artistName

        //Play duration
        showDuration()

        //Descriptions
        durationView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val hasCollectionInfo = !track.collectionName.isNullOrEmpty()
        collectionNameGroupView.isVisible = hasCollectionInfo
        if (hasCollectionInfo) {
            albumView.text = track.collectionName
        }
        releaseDateView.text = track.getReleaseYear()
        genreView.text = track.primaryGenreName
        countryView.text = track.country

    }

    private fun showCurrentDuration() {
        showDuration(playerInteractor.getCurrentPosition())
    }

    private fun showDuration(playDuration: Int = 0) {
        playTimeView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playDuration)
    }

    private fun preparePlayer() {

        val listener = object : PlayerInteractor.PrepareListener {

            override fun onPrepareListener() {
                playTrackBtn.isEnabled = true
            }

            override fun onCompletionListener() {
                playTrackBtn.setImageResource(R.drawable.play_button)
                handler.removeCallbacks(showDurationRunnable)
                showDuration()
            }

        }
        playerInteractor.prepared(
            track = track,
            listener = listener
        )

    }

    private fun startPlayer() {

        playerInteractor.played()
        playTrackBtn.setImageResource(R.drawable.pause_button)
        handler.postDelayed(
            showDurationRunnable, DURATION_DELAY
        )

    }

    private fun pausePlayer() {

        playerInteractor.paused()

        playTrackBtn.setImageResource(R.drawable.play_button)
        handler.removeCallbacks(showDurationRunnable)
    }

    private fun playbacklControl() {
        when (playerInteractor.getPlaybackState()) {
            PlaybackState.PLAYING -> {
                pausePlayer()
            }

            PlaybackState.PREPARED, PlaybackState.PAUSED -> {
                startPlayer()
            }

            PlaybackState.DEFAULT -> {}
        }
    }

    companion object {

        const val CURRENT_TRACK_KEY = "track"
        private const val DURATION_DELAY = 300L

    }

}