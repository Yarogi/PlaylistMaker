package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.main.model.Track

class TrackAdapter(val listener: Listener) : ListAdapter<Track, TrackViewHolder>(ItemComparator()) {


    class ItemComparator : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.trackId == newItem.trackId
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun onClickTrackListener(track: Track)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_item_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        val track = getItem(position)
        holder.bind(track)
        holder.itemView.setOnClickListener {
            listener.onClickTrackListener(track)
        }
    }

}

//class TrackAdapter(val listener: Listener) : RecyclerView.Adapter<TrackViewHolder>() {
//
//    var tracks = ArrayList<Track>()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
//
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.track_item_view, parent, false)
//        return TrackViewHolder(view)
//
//    }
//
//    override fun getItemCount() = tracks.size
//    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
//        holder.bind(tracks[position])
//        holder.itemView.setOnClickListener {
//            listener.onClickTrackListener(tracks[position])
//        }
//    }
//
//    interface Listener {
//        fun onClickTrackListener(track: Track)
//    }
//
//}