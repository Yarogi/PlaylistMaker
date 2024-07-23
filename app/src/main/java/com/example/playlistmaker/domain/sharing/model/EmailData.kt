package com.example.playlistmaker.domain.sharing.model

data class EmailData(
    val recipient: String,
    val theme: String,
    val body: String,
)