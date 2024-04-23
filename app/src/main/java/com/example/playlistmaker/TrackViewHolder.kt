package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.model.Track

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val artworkView: ImageView
    private val artistNameView: TextView
    private val trackNameView: TextView
    private val trackTimeView: TextView

    init {
        artworkView = itemView.findViewById(R.id.artwork)
        artistNameView = itemView.findViewById(R.id.artistName)
        trackNameView = itemView.findViewById(R.id.trackName)
        trackTimeView = itemView.findViewById(R.id.trackTime)
    }

    fun bind(model: Track) {

        artistNameView.text = model.artistName
        trackNameView.text = model.trackName
        trackTimeView.text = model.trackTime

        Glide.with(artworkView)
            .load(model.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(2))
            .into(artworkView)

    }

}