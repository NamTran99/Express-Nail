package com.example.nailexpress.extension

import com.example.nailexpress.extension.Format.FORMAT_TIME_2
import java.text.SimpleDateFormat
import java.util.*

object Format {
    const val DATE_PICKER = "MM/dd/yyyy"
    const val FORMAT_DATE_TIME_API = "yyyy-MM-dd HH:mm"
    const val FORMAT_DATE_TIME_API_2 = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_DATE_MONTH_TIME = "MMMM dd, yyyy hh:mm a"
    const val FORMAT_DATE_MONTH = "MMMM dd, yyyy"
    const val FORMAT_DATE_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val FORMAT_TIME_1 = "hh:mm a"
    const val FORMAT_TIME_2 = "HH:mm:ss"
}

fun String.convertTime(oldFormat: String,newFormat : String): String {
    val inputFormat = SimpleDateFormat(oldFormat, Locale.US)
    val outputFormat = SimpleDateFormat(newFormat, Locale.US)
    val date = inputFormat.parse(this)
    return outputFormat.format(date)
}