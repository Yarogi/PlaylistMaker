package com.example.playlistmaker.presentation.featured

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.featured.api.FeaturedTracksInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class FeaturedTracksViewModel(
    private val featuredInteractor: FeaturedTracksInteractor,
) :
    ViewModel() {

    private val stateLiveData = MutableLiveData<FeaturedTracksState>()
    fun stateObserver(): LiveData<FeaturedTracksState> = stateLiveData

    private var featuredJob: Job? = null

    fun readFeaturedTracks() {

        featuredJob?.cancel()
        featuredJob = viewModelScope.launch {

            featuredInteractor
                .read()
                .flowOn(Dispatchers.IO)
                .collect { featuredTracks ->
                    renderState(
                        when {
                            featuredTracks.isEmpty() -> FeaturedTracksState.Empty
                            else -> FeaturedTracksState.Content(data = featuredTracks)
                        }
                    )
                }
        }

    }

    private fun renderState(state: FeaturedTracksState) {
        stateLiveData.postValue(state)
    }

}