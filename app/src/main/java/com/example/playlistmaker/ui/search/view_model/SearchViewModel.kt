package com.example.playlistmaker.ui.search.view_model

import android.app.Application
import android.content.Context
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
        private const val SEARCH_HISTORY_SIZE = 10
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(this[APPLICATION_KEY] as Application)
                }
            }

    }

    private val handler = Handler(Looper.getMainLooper())

    //LiveData
    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData
    //--LiveData

    //Search
    private var latestSearchText: String? = null
    private val searchInteractor = Creator.provideTracksInteractor()
    //--Search

    //History
    private val history by lazy {
        searchHistoryInteractor.read()
    }
    private val searchHistoryInteractor by lazy {
        Creator.provideSearchHistoryInteractor(
            context = getApplicationContext()
        )
    }
    //--History

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
            renderState(SearchState.HistoryContent(history))
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

        renderState(SearchState.SearchLoading)
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
                    renderState(SearchState.SearchEmpty)
                } else {
                    renderState(SearchState.SearchContent(resource.data))
                }
            }

            is Resource.Error -> renderState(SearchState.SearchError)
        }

    }
    //--Search

    //History

    fun addTrackToHistory(track: Track){

        if (history.isNotEmpty()) {
            val index = history.indexOfFirst { el -> el.trackId == track.trackId }
            if (index >= 0) {
                if (index == 0) {
                    return
                }
                history.removeAt(index)
            }
        }

        history.add(0, track)
        if (history.size > SEARCH_HISTORY_SIZE) {
            while (history.size != SEARCH_HISTORY_SIZE) {
                history.removeLast()
            }
        }

        saveSearchHistory()

    }

    fun replaceTrackInHistory(track: Track){
        if (history.isNotEmpty() && history[0].trackId != track.trackId) {

            val i = history.indexOf(track)

            history.removeAt(i)
            history.add(0, track)

            renderState(SearchState.ReplacedHistory(
                indexFrom = i,
                indexTo = 0,
                tracks = history
            ))

        }
    }

    fun clearHistory(){
        history.clear()
        saveSearchHistory()

        renderState(SearchState.HistoryContent(history))

    }

    private fun saveSearchHistory(){
        searchHistoryInteractor.save(history)
    }
    //--History

    //-----------------------------------------------------------------------------------

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    private fun getApplicationContext(): Context {
        return getApplication<Application>().applicationContext
    }


}