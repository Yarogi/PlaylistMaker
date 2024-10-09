package com.example.playlistmaker.ui.player.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.media_library.playlists.model.Playlist

class PlayerPlaylistAdapter() : RecyclerView.Adapter<PlayerPlaylistHolder>() {

    var playlists = arrayListOf<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerPlaylistHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.player_playlist_item, parent, false)
        return PlayerPlaylistHolder(view)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlayerPlaylistHolder, position: Int) {
        holder.bind(playlists[position])
    }

}