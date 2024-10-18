package com.example.playlistmaker.ui.playlists.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlists.model.Playlist

class PlaylistAdapter(private val listener: Listener) :
    RecyclerView.Adapter<PlaylistViewHolder>() {

    var playlists = arrayListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.playlist_list_item, parent, false)

        return PlaylistViewHolder(view)

    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { listener.onPlaylistClick(playlists[position]) }
    }

    interface Listener {
        fun onPlaylistClick(playlist: Playlist)
    }

}