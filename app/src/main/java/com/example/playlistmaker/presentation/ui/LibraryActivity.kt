package com.example.playlistmaker.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.R

class LibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        val exitButton = findViewById<Button>(R.id.exitBtn)
        exitButton.setOnClickListener {
            this.finish()
        }

    }
}