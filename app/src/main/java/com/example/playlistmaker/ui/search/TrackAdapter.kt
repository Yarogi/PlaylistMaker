package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track

class TrackAdapter(val listener: Listener) : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()
    var hasChange = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)

    }

    override fun getItemCount() = tracks.size
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            listener.onClickTrackListener(tracks[position])
        }
    }

    interface Listener {
        fun onClickTrackListener(track: Track)
    }

}