package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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

    }

}