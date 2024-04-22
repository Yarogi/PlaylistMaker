package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.google.android.material.internal.ViewUtils

class SearchActivity : AppCompatActivity() {

    private var savedSearchText: String = SEARCH_DEF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val exitButton = findViewById<Button>(R.id.exitBtn)
        exitButton.setOnClickListener {
            this.finish()
        }

        val searchTextEdit = getSearchTextEditView()

        val clearSearchText = findViewById<ImageButton>(R.id.clearSearchText)
        clearSearchText.setOnClickListener {
            setTextInSearchEdit(SEARCH_DEF, searchTextEdit)
            hideKeyboard()
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

    private fun getSearchTextEditView(): EditText{
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

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_DEF = ""
    }

}