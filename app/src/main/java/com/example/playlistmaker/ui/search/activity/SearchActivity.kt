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
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.main.model.Track
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.ui.search.model.SearchState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    //MVVM
    private lateinit var viewModel: SearchViewModel
    //

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private var trackSelectionIsProcessed = false

    private var savedSearchText: String = SEARCH_DEF

    private val searchHistoryInteractor by lazy {
        Creator.provideSearchHistoryInteractor(
            context = applicationContext
        )
    }

    //Global-Views
    private lateinit var errorHolderEmpty: View
    private lateinit var errorHolderNoConnection: View
    private lateinit var searchTextEdit: EditText

    //TrackList
    private lateinit var trackListView: RecyclerView
    private val trackListClickListener = object : TrackAdapter.Listener {
        override fun onClickTrackListener(track: Track) {
            addTrackInHistory(track)
            startingTrack(track)
        }

    }
    private val trackListAdapter = TrackAdapter(trackListClickListener)

    //Search history
    private val history = ArrayList<Track>()
    private val historyClickListener = object : TrackAdapter.Listener {
        override fun onClickTrackListener(track: Track) {

            if (trackSelectionIsProcessed) return
            trackSelectionIsProcessed = true

            //replace track in history
            replaceTrackInHistory(track)
            startingTrack(track)

            trackSelectionIsProcessed = false

        }
    }
    private val historyAdapter = TrackAdapter(historyClickListener)
    private lateinit var historyHolder: LinearLayout
    private lateinit var historyListView: RecyclerView

    //Search sync
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            owner = this,
            factory = SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        //Initializing views
        val exitButton = binding.exitBtn
        val clearSearchText = binding.clearSearchText
        searchTextEdit = binding.searchTextEdit
        progressBar = binding.progressBar

        //Exit
        exitButton.setOnClickListener {
            this.finish()
        }

        //Clear text
        clearSearchText.setOnClickListener {
            setTextInSearchEdit(SEARCH_DEF)
            hideKeyboard()

            updateVisibiltyViews(hideList = true)

        }

        //Search text

        searchTextEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchTrackDebounce(
                    searchText = searchTextEdit.text?.toString() ?: ""
                )
                true
            }
            false
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                clearSearchText.visibility = clearButtonVisibility(s)

                //history
                val visibility =
                    if (searchTextEdit.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                updateHistoryVisible(visibility)
                //--history

                //Search track
                viewModel.searchTrackDebounce(
                    searchText = s?.toString() ?: "",
                    useDelay = true
                )

            }

            override fun afterTextChanged(s: Editable?) {}
        }
        searchTextEdit.addTextChangedListener(searchTextWatcher)


        //history
        searchTextEdit.setOnFocusChangeListener { _, hasFocus ->

            val visibility =
                if (hasFocus && searchTextEdit.text.isEmpty()) View.VISIBLE else View.GONE

            updateHistoryVisible(visibility)

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
        viewModel.observeState().observe(this) { state -> render(state) }
        //--observers


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, savedSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedSearchText = savedInstanceState.getString(
            SEARCH_TEXT, SEARCH_DEF
        )
        setTextInSearchEdit(savedSearchText)
        if (savedSearchText != SEARCH_DEF) viewModel.searchTrackDebounce(savedSearchText)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun getTrackListView(): RecyclerView {
        return findViewById(R.id.trackListView)
    }

    private fun setTextInSearchEdit(text: String) {
        searchTextEdit.setText(text)
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
        trackListView = getTrackListView()
        trackListView.adapter = trackListAdapter
    }

    private fun initErrorHolders() {

        val errorGroupView = findViewById<LinearLayout>(R.id.errorHoldersGroup)
        errorHolderEmpty = LayoutInflater.from(this@SearchActivity)
            .inflate(R.layout.search_no_found_view, errorGroupView, false)
        errorHolderNoConnection = LayoutInflater.from(this@SearchActivity)
            .inflate(R.layout.search_no_connection_view, errorGroupView, false)
        val updateBtn = errorHolderNoConnection.findViewById<Button>(R.id.updateButton)
        updateBtn.setOnClickListener {
            viewModel.searchTrackDebounce(searchTextEdit.text?.toString() ?: "")
        }

        errorGroupView.addView(errorHolderEmpty)
        errorGroupView.addView(errorHolderNoConnection)
    }

    private fun initHistoryHolder() {

        historyHolder = findViewById(R.id.historyHolder)
        historyListView = findViewById(R.id.historyListView)

        historyListView.adapter = historyAdapter

        readSavedHistory()
        historyAdapter.tracks = history

        val visibility = if (searchTextEdit.hasFocus()) View.VISIBLE else View.GONE
        updateHistoryVisible(visibility)

        val clearHistoryBtn = findViewById<Button>(R.id.clearHistory)
        clearHistoryBtn.setOnClickListener {
            history.clear()
            saveHistory()
            updateHistoryVisible(View.GONE)
        }


    }

    private fun addTrackInHistory(track: Track) {

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
        if (history.size > 10) {
            while (history.size != 10) {
                history.removeLast()
            }
        }
        saveHistory()
    }

    private fun replaceTrackInHistory(track: Track) {
        if (history.isNotEmpty() && history[0].trackId != track.trackId) {

            val i = history.indexOf(track)

            history.removeAt(i)
            history.add(0, track)

            historyAdapter.notifyItemMoved(i, 0)
            historyAdapter.notifyItemRangeChanged(0, history.size)

            saveHistory()
        }
    }

    private fun startingTrack(track: Track) {

        val json = Gson().toJson(track)

        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(PlayerActivity.CURRENT_TRACK_KEY, json)
        startActivity(intent)

    }

    private fun readSavedHistory() {

        val savedHistory = searchHistoryInteractor.read()
        history.addAll(savedHistory)

    }

    private fun saveHistory(updateAdapter: Boolean = true) {

        historyAdapter.hasChange = updateAdapter
        searchHistoryInteractor.save(history)

    }

    //show_states

    private fun render(state: SearchState) {

        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.traks)
            SearchState.Empty -> showEmptyResult()
            SearchState.Error -> showSomethingWrong()
            is SearchState.History -> updateHistoryVisible()
        }

    }

    private fun updateHistoryVisible(forceVisibileValue: Int? = null) {

        val visibility =
            when {
                history.isEmpty() -> View.GONE
                else -> forceVisibileValue ?: View.VISIBLE
            }

        if (visibility == View.VISIBLE && historyAdapter.hasChange) {
            historyAdapter.notifyDataSetChanged()
        }

        historyHolder.visibility = visibility

    }

    private fun showHistory() {
        updateVisibiltyViews(hideList = true)
    }

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
        noConnection: Boolean = false,
        empty: Boolean = false,
        hideList: Boolean = false,
        showProgressBar: Boolean = false,
    ) {

        errorHolderNoConnection.visibility = View.GONE
        errorHolderEmpty.visibility = View.GONE
        trackListView.visibility = View.GONE
        progressBar.visibility = View.GONE

        when {
            noConnection -> errorHolderNoConnection.visibility = View.VISIBLE
            empty -> errorHolderEmpty.visibility = View.VISIBLE
            showProgressBar -> progressBar.visibility = View.VISIBLE
            !hideList -> trackListView.visibility = View.VISIBLE
        }

    }

    //--show_states


    companion object {

        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_DEF = ""

    }

}