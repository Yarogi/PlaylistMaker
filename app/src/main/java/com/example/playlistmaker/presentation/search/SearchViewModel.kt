package com.example.playlistmaker.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.domain.search.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.TracksInteractor
import com.example.playlistmaker.domain.search.model.TrackSearchStructure
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {

    companion object {
        const val SEARCH_DEF = ""
        private const val SEARCH_DELAY = 2000L
        private const val SEARCH_HISTORY_SIZE = 10

    }

    private var searchJob: Job? = null

    //Search
    private var latestSearchText: String? = null

    private fun getDefaultState(): SearchState {
        return SearchState.NoContent("")
    }

    private val stateLiveData = MutableLiveData<SearchState>(getDefaultState())
    fun observeSearchState(): LiveData<SearchState> = stateLiveData
    //--Search

    //History
    private val history by lazy { searchHistoryInteractor.read() }
    //--History

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

        //Есть вариант, когда необходимо запустить поиск без задержки:
        //например,если пользователь нажал на кнопку окончания ввода
        //поэтому универсальный debounce не используется
        searchJob?.cancel()
        if (searchText.isEmpty()) {
            renderHistory()
        } else {
            renderState(SearchState.NoContent(searchText))
            searchJob = viewModelScope.launch {
                if (useDelay) delay(SEARCH_DELAY)
                renderState(SearchState.Loading(searchText))
                //Поиск
                val searchStructure = TrackSearchStructure(term = searchText)
                launch {
                    searchInteractor.searchTracks(searchStructure = searchStructure)
                        .collect { result ->
                            processSearchResult(result.traks, result.errorMessage)
                        }
                }
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

    private fun processSearchResult(foundTracks: List<Track>?, errorMessage: String?) {

        when {
            errorMessage != null -> renderState(SearchState.Error(getLatestSearchText()))
            foundTracks.isNullOrEmpty() -> renderState(SearchState.Empty(getLatestSearchText()))
            else -> renderState(
                SearchState.Content(
                    searchText = getLatestSearchText(),
                    tracks = foundTracks
                )
            )
        }

    }

    private fun getLatestSearchText(): String {
        return latestSearchText ?: SEARCH_DEF
    }
    //--Search

    //History

    /** Отображение истории при смене фокуса */
    fun readSearchHistoryDebounce(hasFocus: Boolean) {

        val currentSearchText = getLatestSearchText()
        if (currentSearchText.isEmpty()) {
            if (hasFocus) {
                renderHistory()
            } else {
                renderState(SearchState.NoContent(currentSearchText))
            }
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
        //Если текст поиска пустой, то обновляем отображение истории
        if (getLatestSearchText().isEmpty()) {
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