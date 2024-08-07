package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.ui.search.model.SearchState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModel<SearchViewModel>()

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private var trackSelectionIsProcessed = false

    //Dinamic-views
    private lateinit var errorHolderEmpty: View
    private lateinit var errorHolderNoConnection: View

    //TrackList
    private val trackListClickListener = object : TrackAdapter.Listener {
        override fun onClickTrackListener(track: Track) {
            viewModel.addTrackToHistory(track)
            startingTrack(track)
        }

    }
    private val trackListAdapter = TrackAdapter(trackListClickListener)

    //Search history
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


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Exit
        binding.exitBtn.setOnClickListener {
            this.finish()
        }

        //Clear text
        binding.clearSearchText.setOnClickListener {

            hideKeyboard()
            viewModel.searchTrackDebounce("")

        }

        //Search text
        binding.searchTextEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchTrackDebounce(
                    searchText = binding.searchTextEdit.text?.toString() ?: ""
                )
                true
            }
            false
        }

        val searchTextWatcher = object : TextWatcher {
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
        binding.searchTextEdit.addTextChangedListener(searchTextWatcher)

        //history
        binding.searchTextEdit.setOnFocusChangeListener { _, hasFocus ->

            viewModel.readSearchHistoryDebounce(hasFocus)

        }

        //Errors holders
        initErrorHolders()
        //TrackList
        initTrackList()
        //History holder
        initHistoryHolder()
        //Visibility
        updateVisibiltyViews()

        //observers
        viewModel.observeSearchState().observe(this) { state -> render(state) }
        //--observers

    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun initTrackList() {
        binding.trackListView.adapter = trackListAdapter
    }

    private fun initErrorHolders() {

        val errorGroupView = binding.errorHoldersGroup

        errorHolderEmpty = LayoutInflater.from(this@SearchActivity)
            .inflate(R.layout.search_no_found_view, errorGroupView, false)
        errorHolderNoConnection = LayoutInflater.from(this@SearchActivity)
            .inflate(R.layout.search_no_connection_view, errorGroupView, false)
        val updateBtn = errorHolderNoConnection.findViewById<Button>(R.id.updateButton)
        updateBtn.setOnClickListener {
            viewModel.searchTrackDebounce(
                searchText = binding.searchTextEdit.text?.toString() ?: "",
                forceMode = true
            )
        }

        errorGroupView.addView(errorHolderEmpty)
        errorGroupView.addView(errorHolderNoConnection)
    }

    private fun initHistoryHolder() {

        binding.historyListView.adapter = historyAdapter

        val clearHistoryBtn = findViewById<Button>(R.id.clearHistory)
        clearHistoryBtn.setOnClickListener {
            viewModel.clearHistory()
        }

    }

    //??
    private fun startingTrack(track: Track) {

        val json = Gson().toJson(track)

        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(PlayerActivity.CURRENT_TRACK_KEY, json)
        startActivity(intent)

    }

    //show_states

    private fun render(state: SearchState) {

        if (state.searchText != binding.searchTextEdit.text.toString()) {
            binding.searchTextEdit.setText(state.searchText)
        }

        binding.clearSearchText.isVisible = state.searchText.isNotEmpty()

        when (state) {
            //search
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Empty -> showEmptyResult()
            is SearchState.Error -> showSomethingWrong()
            //history
            is SearchState.History -> showHistory(state.tracks)
            is SearchState.NoContent -> showEmptyContent()
        }

    }

    private fun showEmptyContent() {
        updateVisibiltyViews()
    }

    //history
    private fun showHistory(tracks: List<Track>) {

        if (historyAdapter.tracks.isNotEmpty()) historyAdapter.tracks.clear()
        historyAdapter.tracks.addAll(tracks)
        historyAdapter.notifyDataSetChanged()

        updateVisibiltyViews(showHistory = true)

    }
    //--history

    private fun showContent(foundTracks: List<Track>) {

        trackListAdapter.tracks.clear()
        trackListAdapter.tracks.addAll(foundTracks)
        trackListAdapter.notifyDataSetChanged()

        updateVisibiltyViews(hideList = false)

    }

    private fun showLoading() {
        updateVisibiltyViews(showProgressBar = true)
    }

    private fun showEmptyResult() {
        updateVisibiltyViews(empty = true)
    }

    private fun showSomethingWrong() {
        updateVisibiltyViews(noConnection = true)
    }

    private fun updateVisibiltyViews(
        noConnection: Boolean? = null,
        empty: Boolean? = null,
        hideList: Boolean? = null,
        showProgressBar: Boolean? = null,
        showHistory: Boolean? = null,
    ) {

        errorHolderNoConnection.isVisible = noConnection ?: false
        errorHolderEmpty.isVisible = empty ?: false
        binding.trackListView.isVisible = !(hideList ?: true)
        binding.progressBar.isVisible = showProgressBar ?: false
        binding.historyHolder.isVisible = showHistory ?: false && historyAdapter.tracks.isNotEmpty()

    }

}