package com.example.playlistmaker

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.model.Track

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