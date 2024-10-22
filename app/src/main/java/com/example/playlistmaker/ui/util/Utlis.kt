package com.example.playlistmaker.ui.util

import android.content.Context
import android.util.TypedValue
import com.example.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

fun pxToDP(context: Context, value: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(), context.resources.displayMetrics
    ).toInt()
}

fun tracksQuantityToString(context: Context, tracksQuantity: Int): String {
    var trackPost =
        when (tracksQuantity % 10) {
            1 -> context.getString(R.string.track)
            in 2..4 -> context.getString(R.string.track_genitive)
            else -> context.getString(R.string.tracks_genitive)
        }
    return "${tracksQuantity} $trackPost"
}

fun trackDurationToString(context: Context, duration: Float): String {

    val minutes = Math.round(duration / 60000)
    val post: String = when (minutes % 100) {
        in 11..14 -> context.getString(R.string.minutes_genitive)
        else -> when (minutes % 10) {
            1 -> context.getString(R.string.minute)
            in 2..4 -> context.getString(R.string.minutes)
            else -> context.getString(R.string.minutes_genitive)
        }
    }

    return "$minutes $post"

}

fun trackDurationToTimeString(duration: Float): String {

    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)

}