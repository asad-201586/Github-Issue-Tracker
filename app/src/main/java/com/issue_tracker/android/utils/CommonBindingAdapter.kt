package com.issue_tracker.android.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("changeTimeFormat")
fun TextView.changeTimeFormat(time: String?) {
    if (time != null) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault())
        var formattedDateTime = ""
        try {
            val date = inputFormat.parse(time)
            formattedDateTime = outputFormat.format(date)
            text = formattedDateTime
        } catch (e: Exception) {
            Timber.e("change_time_format ${e.message}")
            text = time
        }
    } else {
        text = ""
    }
}