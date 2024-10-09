package com.example.playlistmaker.ui.player.playlists

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.media_library.playlists.model.Playlist
import com.example.playlistmaker.ui.util.pxToDP
import com.example.playlistmaker.ui.util.tracksQuantityToString

class PlayerPlaylistHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val coverView: ImageView = itemView.findViewById(R.id.cover)
    private val nameView: TextView = itemView.findViewById(R.id.name)
    private val tracksQuantityView: TextView = itemView.findViewById(R.id.tracks_quantity)

    fun bind(playlist: Playlist) {

        nameView.text = playlist.name
        tracksQuantityView.text = tracksQuantityToString(playlist.tracksQuantity)

        Glide.with(itemView)
            .load(playlist.coverPathUri)
            .placeholder(R.drawable.track_placeholder)
            .transform(CenterCrop(), RoundedCorners(pxToDP(itemView.context, 2)))
            .into(coverView)
    }


}