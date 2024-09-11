package com.example.playlistmaker.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.presentation.search.SearchState
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.ui.player.PlayerActivity
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    //Search
    private lateinit var textWatcher: TextWatcher

    //Tracklist
    private val trackListClickListener = object : TrackAdapter.Listener {
        override fun onClickTrackListener(track: Track) {
            playTrackDebouncer(track)
        }

    }
    private val trackListAdapter = TrackAdapter(trackListClickListener)

    //History
    private val historyClickListener = object : TrackAdapter.Listener {
        override fun onClickTrackListener(track: Track) {
            playTrackDebouncer(track)
        }
    }
    private val historyAdapter = TrackAdapter(historyClickListener)

    private val playTrackDebouncer: (Track) -> Unit by lazy {
        debounce(0L, lifecycleScope, false) { track ->
            viewModel.addTrackToHistory(track)
            findNavController().navigate(
                R.id.action_searchFragment_to_playerActivity,
                PlayerActivity.createArgs(track)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEditSearchTextView()
        initSearchResultView()
        initErrorHoldersView()
        initHistoryHolderView()

        //observers
        viewModel.observeSearchState().observe(viewLifecycleOwner) { renderState(it) }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        textWatcher?.let {
            binding.searchTextEdit.removeTextChangedListener(it)
        }

        _binding = null

        Log.d("SEARCH_FRAGMENT", "Destroy view $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SEARCH_FRAGMENT", "Destroy $this")
    }

    /** Инициализация элементов поиска и установка слушателей */
    private fun initEditSearchTextView() {

        //Clear text
        binding.clearSearchText.setOnClickListener {
            hideKeyboard()
            viewModel.searchTrackDebounce("")
        }

        //Keyboard done
        binding.searchTextEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //Заершение ввода не использует задержку
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