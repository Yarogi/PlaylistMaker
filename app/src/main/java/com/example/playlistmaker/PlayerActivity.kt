package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.model.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    lateinit var track: Track
    var playDuration = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backBtn = findViewById<ImageButton>(R.id.panelBackArrow)
        backBtn.setOnClickListener { finish() }

        val json = intent.getStringExtra(CURRENT_TRACK_KEY)
        track = Gson().fromJson(json, Track::class.java)
        fill()

    }

    private fun fill() {

        //Views: Cover image
        val coverImgView = findViewById<ImageView>(R.id.cover)

        //Views: Track title
        val trackNameView = findViewById<TextView>(R.id.trackName)
        val artistNameView = findViewById<TextView>(R.id.artistName)

        //Views: Controls
        val addPlaylistBtn = findViewById<ImageButton>(R.id.addPlaylist)
        val playTrackBtn = findViewById<ImageButton>(R.id.playTrack)
        val addFavoriteBtn = findViewById<ImageButton>(R.id.like)

        //View: Play duration
        val playTimeView = findViewById<TextView>(R.id.playTime)

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
        playTimeView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playDuration)

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

    companion object {
        const val CURRENT_TRACK_KEY = "track"
    }

}