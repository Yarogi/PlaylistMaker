package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.model.Track
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var trackSelectionIsProcessed = false

    private var savedSearchText: String = SEARCH_DEF
    private val trackList = ArrayList<Track>()

    //Retrofit - GSON
    private val trackBaseUrl = "https://itunes.apple.com";
    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackSearchService = retrofit.create(TrackSearchApi::class.java)

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
    private val searchRunnable = Runnable { searchTrack() }
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Initializing views
        val exitButton = findViewById<Button>(R.id.exitBtn)
        val clearSearchText = findViewById<ImageButton>(R.id.clearSearchText)
        searchTextEdit = findViewById(R.id.searchTextEdit)
        progressBar = findViewById(R.id.progressBar)

        //Exit
        exitButton.setOnClickListener {
            this.finish()
        }

        //Clear text
        clearSearchText.setOnClickListener {
            setTextInSearchEdit(SEARCH_DEF)
            hideKeyboard()

            trackList.clear()
            updateVisibiltyViews(hideList = true)

        }

        //Search text
        searchTextEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
                true
            }
            false
        }
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                savedSearchText = if (s.isNullOrEmpty()) SEARCH_DEF else s.toString()
                clearSearchText.visibility = clearButtonVisibility(s)

                val visibility =
                    if (searchTextEdit.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                updateHistoryVisible(visibility)

                //Search track
                searchTrackDebounce()

            }

            override fun afterTextChanged(s: Editable?) {}
        }
        searchTextEdit.addTextChangedListener(searchTextWatcher)
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
        if (savedSearchText != SEARCH_DEF) searchTrack()
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

    private fun updateVisibiltyViews(
        noConnection: Boolean = false,
        empty: Boolean = false,
        hideList: Boolean = false,
        showProgressBar: Boolean = false
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

    private fun initTrackList() {
        trackListView = getTrackListView()
        trackListView.adapter = trackListAdapter
        trackListAdapter.tracks = trackList
    }

    private fun initErrorHolders() {

        val errorGroupView = findViewById<LinearLayout>(R.id.errorHoldersGroup)
        errorHolderEmpty = LayoutInflater.from(this@SearchActivity)
            .inflate(R.layout.search_no_found_view, errorGroupView, false)
        errorHolderNoConnection = LayoutInflater.from(this@SearchActivity)
            .inflate(R.layout.search_no_connection_view, errorGroupView, false)
        val updateBtn = errorHolderNoConnection.findViewById<Button>(R.id.updateButton)
        updateBtn.setOnClickListener {
            searchTrack()
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

    private fun searchTrackDebounce() {

        mainHandler.removeCallbacks(searchRunnable)

        if (searchTextEdit.text.isNotEmpty()) {
            mainHandler.postDelayed(searchRunnable, SEARCH_DELAY)
        } else {
            progressBar.visibility = View.GONE
        }

    }

    private fun searchTrack() {

        updateVisibiltyViews(
            showProgressBar = true
        )

        trackSearchService.search(savedSearchText).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(
                call: Call<TrackResponse>, response: Response<TrackResponse>
            ) {

                if (searchTextEdit.text.isEmpty()) {
                    return
                }

                if (response.code() == 200) {

                    trackList.clear()

                    if (response.body()?.results?.isNotEmpty() == true) {
                        trackList.addAll(response.body()?.results!!)
                        trackListAdapter.notifyDataSetChanged()
                    }

                    updateVisibiltyViews(false, trackList.isEmpty())

                } else {
                    showSomethingWrong()
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {

                showSomethingWrong()

            }
        })
    }

    private fun showSomethingWrong() {
        updateVisibiltyViews(true)
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
        val json = getSearchPref().getString(HISTORY_KEY, null) ?: return
        history.addAll(Gson().fromJson(json, Array<Track>::class.java))
    }

    private fun saveHistory(updateAdapter: Boolean = true) {

        historyAdapter.hasChange = updateAdapter

        val json = Gson().toJson(history)
        getSearchPref().edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

    private fun getSearchPref() = getSharedPreferences(SEARCH_PREFERENCES, MODE_PRIVATE)

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_DEF = ""

        //search preferences
        const val SEARCH_PREFERENCES = "playlistmaker_search_preferences"
        const val HISTORY_KEY = "search_history"

        //search debounce
        private const val SEARCH_DELAY = 2000L
    }

}