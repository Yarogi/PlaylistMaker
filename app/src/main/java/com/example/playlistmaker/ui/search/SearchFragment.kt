package com.example.playlistmaker.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.presentation.search.SearchState
import com.example.playlistmaker.presentation.search.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()

    private lateinit var binding: FragmentSearchBinding

    //Search
    private lateinit var textWatcher: TextWatcher

    //Tracklist
    private val trackListClickListener = object : TrackAdapter.Listener {
        override fun onClickTrackListener(track: Track) {
            viewModel.addTrackToHistory(track)
            startingTrack(track)
        }

    }
    private val trackListAdapter = TrackAdapter(trackListClickListener)

    //History
    private var trackSelectionIsProcessed = false
    private val historyClickListener = object : TrackAdapter.Listener {
        override fun onClickTrackListener(track: Track) {

            if (trackSelectionIsProcessed) return
            trackSelectionIsProcessed = true

            //replace track in history
            viewModel.replaceTrackInHistory(track)
            startingTrack(track)

            trackSelectionIsProcessed = false

        }
    }
    private val historyAdapter = TrackAdapter(historyClickListener)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEditSearchTextView()
        initSearchResultView()
        initErrorHoldersView()
        initHistoryHolderView()

        updateResultViewVisible()

        //observers
        viewModel.observeSearchState().observe(viewLifecycleOwner) { renderState(it) }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        textWatcher?.let {
            binding.searchTextEdit.removeTextChangedListener(it)
        }

    }

    private fun initEditSearchTextView() {

        //Clear text
        binding.clearSearchText.setOnClickListener {
            hideKeyboard()
            viewModel.searchTrackDebounce("")
        }

        //Done in keyboard
        binding.searchTextEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchTrackDebounce(
                    searchText = binding.searchTextEdit.text?.toString() ?: ""
                )
                true
            }
            false
        }

        //Text watcher
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                //Search track
                viewModel.searchTrackDebounce(
                    searchText = s?.toString() ?: "",
                    useDelay = true
                )

            }

            override fun afterTextChanged(s: Editable?) {}
        }
        textWatcher?.let {
            binding.searchTextEdit.addTextChangedListener(it)
        }

        //OnFocus
        binding.searchTextEdit.setOnFocusChangeListener { _, hasFocus ->
            viewModel.readSearchHistoryDebounce(hasFocus)
        }

    }

    private fun initSearchResultView() {
        binding.trackListView.adapter = trackListAdapter
    }

    private fun initErrorHoldersView() {
        binding.updateButton.setOnClickListener {
            viewModel.searchTrackDebounce(
                searchText = binding.searchTextEdit.text?.toString() ?: "",
                forceMode = true
            )
        }
    }

    private fun initHistoryHolderView() {
        binding.historyListView.adapter = historyAdapter
        binding.clearHistory.setOnClickListener {
            viewModel.clearHistory()
        }
    }

    private fun hideKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val inputMethodManager =
                requireContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun startingTrack(track: Track) {
        Toast.makeText(
            requireContext(),
            "Вопроизводим ${track.trackName}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun renderState(state: SearchState) {

        updateEditSearchTextState(state)

        when (state) {
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Empty -> showEmpty()
            is SearchState.Error -> showError()
            is SearchState.History -> showHistory(state.tracks)
            is SearchState.Loading -> showLoading()
            is SearchState.NoContent -> showEmptyContent()
        }
    }

    private fun updateEditSearchTextState(state: SearchState) {
        if (state.searchText != binding.searchTextEdit.text.toString()) {
            binding.searchTextEdit.setText(state.searchText)
        }

        binding.clearSearchText.isVisible = state.searchText.isNotEmpty()
    }

    private fun showContent(tracks: List<Track>) {
        trackListAdapter.tracks.clear()
        trackListAdapter.tracks.addAll(tracks)
        trackListAdapter.notifyDataSetChanged()

        updateResultViewVisible(trackList = true)

    }

    private fun showEmpty() {
        updateResultViewVisible(empty = true)
    }

    private fun showError() {
        updateResultViewVisible(noConnection = true)
    }

    private fun showHistory(tracks: List<Track>) {

        if (historyAdapter.tracks.isNotEmpty()) {
            historyAdapter.tracks.clear()
        }

        historyAdapter.tracks.addAll(tracks)
        historyAdapter.notifyDataSetChanged()

        updateResultViewVisible(history = true)

    }

    private fun showLoading() {
        updateResultViewVisible(progressBar = true)
    }

    private fun showEmptyContent() {
        updateResultViewVisible()
    }

    private fun updateResultViewVisible(
        empty: Boolean = false,
        noConnection: Boolean = false,
        progressBar: Boolean = false,
        history: Boolean = false,
        trackList: Boolean = false,
    ) {

        binding.emptyHolder.isVisible = empty
        binding.noConnectionHolder.isVisible = noConnection
        binding.progressBar.isVisible = progressBar
        binding.historyHolder.isVisible = history && historyAdapter.tracks.isNotEmpty()
        binding.trackListView.isVisible = trackList


    }

}