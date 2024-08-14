package com.example.playlistmaker.ui.media_library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.media_library.fragments.FeaturedTracksFragment
import com.example.playlistmaker.ui.media_library.fragments.PlaylistFragment

class LibraryPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            FeaturedTracksFragment.newInstanse()
        else
            PlaylistFragment.newInstanse()
    }
}