package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.model.Track
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var savedSearchText: String = SEARCH_DEF
    private val trackList = ArrayList<Track>()

    //Retrofit - GSON
    private val trackBaseUrl = "https://itunes.apple.com";
    private val retrofit =
        Retrofit.Builder().baseUrl(trackBaseUrl).addConverterFactory(
            GsonConverterFactory.create()
        )
            .build()
    val trackSearchService = retrofit.create(TrackSearchApi::class.java)

    //Global-Views
    private lateinit var errorHolderEmpty: View
    private lateinit var errorHolderNoConnection: View

    private lateinit var trackListView: RecyclerView
    private val trackListAdapter = TrackAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Initializing views
        val exitButton = findViewById<Button>(R.id.exitBtn)
        val searchTextEdit = getSearchTextEditView()
        val clearSearchText = findViewById<ImageButton>(R.id.clearSearchText)

        //Exit
        exitButton.setOnClickListener {
            this.finish()
        }

        //Clear text
        clearSearchText.setOnClickListener {
            setTextInSearchEdit(SEARCH_DEF, searchTextEdit)
            hideKeyboard()
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
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        searchTextEdit.addTextChangedListener(searchTextWatcher)

        //Errors holders
        val errorGroupView = findViewById<LinearLayout>(R.id.errorHoldersGroup)
        errorHolderEmpty = LayoutInflater.from(this@SearchActivity)
            .inflate(R.layout.search_no_found_view, errorGroupView, false)
        errorHolderNoConnection = LayoutInflater.from(this@SearchActivity)
            .inflate(R.layout.search_no_connection_view, errorGroupView, false)

        errorGroupView.addView(errorHolderEmpty)
        errorGroupView.addView(errorHolderNoConnection)

        //TrackList
        trackListView = getTrackListView()
        trackListView.adapter = trackListAdapter
        trackListAdapter.tracks = trackList
        //fillTrackList(testTrackList)

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
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun getSearchTextEditView(): EditText {
        return findViewById<EditText>(R.id.searchTextEdit)
    }

    private fun getTrackListView(): RecyclerView {
        return findViewById<RecyclerView>(R.id.trackListView)
    }

    private fun setTextInSearchEdit(text: String, searchTextEdit: EditText? = null) {
        val textEdit = searchTextEdit ?: getSearchTextEditView()
        textEdit.setText(text)
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
        noConnection: Boolean = false, empty: Boolean = false, hideList: Boolean = false
    ) {

        errorHolderNoConnection.visibility = View.GONE
        errorHolderEmpty.visibility = View.GONE
        trackListView.visibility = View.GONE

        if (noConnection) {
            errorHolderNoConnection.visibility = View.VISIBLE
        } else if (empty) {
            errorHolderEmpty.visibility = View.VISIBLE
        } else if (!hideList) {
            trackListView.visibility = View.VISIBLE
        }

    }

    private fun searchTrack() {
        trackSearchService.search(savedSearchText).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(
                call: Call<TrackResponse>, response: Response<TrackResponse>
            ) {

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

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_DEF = ""
    }

}