package com.example.playlistmaker.ui.media_library.playlists.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.media_library.playlists.model.Playlist

class PlaylistViewHolder(view: View) : ViewHolder(view) {

    private val coverView: ImageView = itemView.findViewById(R.id.cover)
    private val nameView: TextView = itemView.findViewById(R.id.name)
    private val tracksQuantityView: TextView = itemView.findViewById(R.id.tracks_quantity)

    fun bind(playlist: Playlist) {

        nameView.text = playlist.name
        tracksQuantityView.text = tracksQuantityToString(playlist.tracksQuantity)

    }

    private fun tracksQuantityToString(tracksQuantity: Int): String {
        var trackPost =
            when (tracksQuantity % 10) {
                1 -> "трек"
                in 2..4 -> "трека"
                else -> "треков"
            }
        return "${tracksQuantity} $trackPost"
    }

}