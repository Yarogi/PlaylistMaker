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
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.search.model.Resource
import com.example.playlistmaker.domain.search.model.TrackSearchStructure
import com.example.playlistmaker.ui.search.model.SearchState
import java.util.concurrent.atomic.AtomicInteger

class SearchViewModel(
    private val application: Application,
    private val searchInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
) : AndroidViewModel(application) {

    companion object {
        const val SEARCH_DEF = ""
        private const val SEARCH_DELAY = 2000L
        private const val SEARCH_HISTORY_SIZE = 10
        private val SEARCH_REQUEST_TOKEN = "SEARCH_TRACK_REQUEST"

        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val app = this[APPLICATION_KEY] as Application
                    SearchViewModel(
                        application = app,
                        searchInteractor = Creator.provideTracksInteractor(),
                        searchHistoryInteractor = Creator.provideSearchHistoryInteractor(app.applicationContext)
                    )
                }
            }

    }

    private val handler = Handler(Looper.getMainLooper())

    //Search
    private var latestSearchHasFocus: Boolean? = null
    private var latestSearchText: String? = null

    private var searchResultDebouncer = AtomicInteger(0)

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeSearchState(): LiveData<SearchState> = stateLiveData
    //--Search

    //History
    private val history by lazy { searchHistoryInteractor.read() }
    //--History

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    //Search
    fun searchTrackDebounce(
        searchText: String,
        useDelay: Boolean = false,
        forceMode: Boolean = false,
    ) {

        if (!forceMode && searchText == latestSearchText) {
            return
        }

        this.latestSearchText = searchText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        var searchCounter = searchResultDebouncer.get()
        searchCounter++
        if (searchCounter >= 100) {
            searchCounter = 0
        }
        searchResultDebouncer.set(searchCounter)

        if (searchText.isEmpty()) {
            renderHistory()
        } else {

            renderState(SearchState.Loading(getLatestSearchText()))

            val searchRunnable = Runnable { searchRequest(searchText, searchCounter) }
            if (useDelay) {
                val postTime = SystemClock.uptimeMillis() + SEARCH_DELAY
                handler.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN, postTime)
            } else {
                handler.postAtFrontOfQueue(searchRunnable)
            }
        }

    }

    private fun renderHistory() {
        renderState(
            if (history.isEmpty())
                SearchState.NoContent(getLatestSearchText())
            else SearchState.History(
                searchText = getLatestSearchText(),
                tracks = history
            )
        )
    }

    private fun searchRequest(searchText: String, searchCounter: Int) {

        val searchStructure = TrackSearchStructure(term = searchText)
        searchInteractor.searchTracks(
            searchStructure = searchStructure,
            consumer = object : TracksInteractor.TracksConsumer {
                override fun consume(resource: Resource<List<Track>>) {
                    processSearchResult(resource, searchCounter)
                }

            })

    }

    private fun processSearchResult(resource: Resource<List<Track>>, searchCounter: Int) {

        if (searchCounter != searchResultDebouncer.get()) return

        when (resource) {
            is Resource.Success -> {
                if (resource.data.isEmpty()) {
                    renderState(SearchState.Empty(getLatestSearchText()))
                } else {
                    renderState(
                        SearchState.Content(
                            searchText = getLatestSearchText(),
                            tracks = resource.data
                        )
                    )
                }
            }

            is Resource.Error -> renderState(SearchState.Error(getLatestSearchText()))
        }

    }

    private fun getLatestSearchText(): String {
        return latestSearchText ?: SEARCH_DEF
    }
    //--Search

    //History

    fun readSearchHistoryDebounce(hasFocus: Boolean) {

        if (hasFocus == latestSearchHasFocus) return

        this.latestSearchHasFocus = hasFocus

        if (hasFocus && latestSearchText?.isEmpty() != false) {
            renderHistory()
        } else {
            renderState(SearchState.NoContent(getLatestSearchText()))
        }

    }

    fun addTrackToHistory(track: Track) {

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

    fun replaceTrackInHistory(track: Track) {
        if (history.isNotEmpty() && history[0].trackId != track.trackId) {

            val i = history.indexOf(track)

            history.removeAt(i)
            history.add(0, track)

            renderHistory()

        }
    }

    fun clearHistory() {

        history.clear()
        saveSearchHistory()

        renderHistory()

    }

    private fun saveSearchHistory() {
        searchHistoryInteractor.save(history)
    }
    //--History

    //-----------------------------------------------------------------------------------

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }


}