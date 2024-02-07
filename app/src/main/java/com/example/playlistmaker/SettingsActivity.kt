package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val exitButton = findViewById<Button>(R.id.exitBtn)
        exitButton.setOnClickListener{
            // //Переход на главный экран я закомменировал, так как забивает стек,
            // // реализовал просто через закрытие
            //val mainIntent = Intent(this, MainActivity::class.java)
            //startActivity(mainIntent)
            //
            this.finish()
        }

    }
}