package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.model.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var savedSearchText: String = SEARCH_DEF

    //Retrofit - GSON
    private val trackBaseUrl = "https://itunes.apple.com";
    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val trackSearchService = retrofit.create(TrackSearchApi::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Initializing views
        val exitButton = findViewById<Button>(R.id.exitBtn)
        val searchTextEdit = getSearchTextEditView()
        val clearSearchText = findViewById<ImageButton>(R.id.clearSearchText)
        val trackListView = findViewById<RecyclerView>(R.id.trackListView)

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

        //TrackList
        val trackListContent = testTrackList
        trackListView.adapter = TrackAdapter(trackListContent)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, savedSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedSearchText = savedInstanceState.getString(
            SEARCH_TEXT,
            SEARCH_DEF
        )

        setTextInSearchEdit(savedSearchText)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun getSearchTextEditView(): EditText {
        return findViewById<EditText>(R.id.searchTextEdit)
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

    private val testTrackList = listOf<Track>(
        Track(
            "Smells Like Teen Spirit",
            "Nirvana",
            "5:01",
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        ),
        Track(
            "Billie Jean",
            "Michael Jackson",
            "4:35",
            " https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
        ),
        Track(
            "Stayin' Alive",
            "Bee Gees",
            "4:10",
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
        ),
        Track(
            "Whole Lotta Love",
            "Led Zeppelin",
            "5:33",
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
        ),
        Track(
            "Sweet Child O'Mine",
            "Guns N' Roses",
            "5:03",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
        )
    )

    private fun searchTrack() {
        trackSearchService.search(savedSearchText)
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    Toast.makeText(
                        this@SearchActivity,
                        "GOOD",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    Toast.makeText(
                        this@SearchActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_DEF = ""
    }

}