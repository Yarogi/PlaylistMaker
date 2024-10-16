package com.example.playlistmaker.ui.util

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale

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

fun trackDurationToString(duration: Float): String {

    val minutes = Math.round(duration / 60000)
    val post: String = when (minutes % 100) {
        in 11..14 -> "минут"
        else -> when (minutes % 10) {
            1 -> "минута"
            in 2..4 -> "минуты"
            else -> "минут"
        }

    }

    return "$minutes $post"

}

fun trackDurationToTimeString(duration: Float): String {

    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)

}