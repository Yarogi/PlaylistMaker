package com.example.playlistmaker.ui.playlists.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.ui.search.TrackViewHolder

class PlaylistTrackAdapter(val listener: Listener) : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_item_view, parent, false)
        return TrackViewHolder(view)

    }

    override fun getItemCount() = tracks.size
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            listener.onClickTrackListener(tracks[position])
        }
        holder.itemView.setOnLongClickListener {
            listener.onLongClickListener(tracks[position])
            true
        }
    }

    interface Listener {
        fun onClickTrackListener(track: Track)
        fun onLongClickListener(track: Track)
    }
}