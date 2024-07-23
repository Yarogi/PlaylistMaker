package com.example.playlistmaker.ui.search.model

import com.example.playlistmaker.domain.main.model.Track

sealed class SearchState(val searchText: String) {

    //Search
    class Loading(searchText: String) : SearchState(searchText)
    class Empty(searchText: String) : SearchState(searchText)
    class Error(searchText: String) : SearchState(searchText)
    class Content(searchText: String, val tracks: List<Track>) : SearchState(searchText)
    class History(searchText: String, val tracks: List<Track>) : SearchState(searchText)
    class NoContent(searchText: String) : SearchState(searchText)

}