package com.example.playlistmaker.ui.util

import android.content.Context
import android.util.TypedValue

fun pxToDP(context: Context, value: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(), context.resources.displayMetrics
    ).toInt()
}

fun tracksQuantityToString(tracksQuantity: Int): String {
    var trackPost =
        when (tracksQuantity % 10) {
            1 -> "трек"
            in 2..4 -> "трека"
            else -> "треков"
        }
    return "${tracksQuantity} $trackPost"
}