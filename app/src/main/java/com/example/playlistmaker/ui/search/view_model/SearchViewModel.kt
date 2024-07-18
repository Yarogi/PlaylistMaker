package com.example.playlistmaker.ui.search.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.search.model.Resource
import com.example.playlistmaker.domain.search.model.TrackSearchStructure
import com.example.playlistmaker.ui.search.model.SearchState

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(this[APPLICATION_KEY] as Application)
                }
            }

    }

    //LiveData
    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData
    //--LiveData

    private val handler = Handler(Looper.getMainLooper())

    //Search
    private var latestSearchText: String? = null
    private val searchInteractor = Creator.provideTracksInteractor()
    //--Search

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    //Search
    fun searchTrackDebounce(searchText: String, useDelay: Boolean = false) {

        if (searchText == latestSearchText) {
            return
        }

        this.latestSearchText = searchText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        if (searchText.isEmpty()) {
            renderState(SearchState.History())
        } else {
            val searchRunnable = Runnable { searchRequest(searchText) }
            if (useDelay) {
                val postTime = SystemClock.uptimeMillis() + SEARCH_DELAY
                handler.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN, postTime)
            } else {
                handler.postAtFrontOfQueue(searchRunnable)
            }
        }

    }

    private fun searchRequest(searchText: String) {

        renderState(SearchState.Loading)
        val searchStructure = TrackSearchStructure(term = searchText)
        searchInteractor.searchTracks(
            searchStructure = searchStructure,
            consumer = object : TracksInteractor.TracksConsumer {
                override fun consume(resource: Resource<List<Track>>) {
                    processSearchResult(resource)
                }

            })

    }

    private fun processSearchResult(resource: Resource<List<Track>>) {

        when (resource) {
            is Resource.Success -> {
                if (resource.data.isEmpty()) {
                    renderState(SearchState.Empty)
                } else {
                    renderState(SearchState.Content(resource.data))
                }
            }

            is Resource.Error -> renderState(SearchState.Error)
        }

    }
    //--Search


    //-----------------------------------------------------------------------------------

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }


}