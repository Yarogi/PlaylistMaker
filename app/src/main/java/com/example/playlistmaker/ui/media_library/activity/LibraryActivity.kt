package com.example.playlistmaker.ui.media_library.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.example.playlistmaker.databinding.ActivityMainBinding

class LibraryActivity : AppCompatActivity() {

    private val binding by lazy {
       ActivityLibraryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.exitBtn.setOnClickListener {
            this.finish()
        }

    }
}