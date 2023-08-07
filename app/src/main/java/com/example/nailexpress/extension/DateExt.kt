package com.example.nailexpress.extension

import android.annotation.SuppressLint
import com.example.nailexpress.extension.Format.DATE_PICKER
import com.example.nailexpress.extension.Format.FORMAT_DATE_TIME_API
import com.example.nailexpress.extension.Format.FORMAT_TIME_1
import java.text.SimpleDateFormat
import java.util.*

object Format {
    const val DATE_PICKER = "MM/dd/yyyy"
    const val FORMAT_DATE_TIME_API = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_DATE_TIME_API_2 = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_DATE_MONTH_TIME = "MMMM dd, yyyy hh:mm a"
    const val FORMAT_DATE_DISPLAY = "hh:mm a - MMMM dd yyyy"
    const val FORMAT_DATE_MONTH = "MMMM dd, yyyy"
    const val FORMAT_DATE_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val FORMAT_TIME_1 = "hh:mm a"
    const val FORMAT_TIME_2 = "HH:mm:ss"
}

fun String.toDateWithFormat(
    formatInput: String = DATE_PICKER,
    formatOutput: String = DATE_PICKER
): Int {
    val inputFormat = SimpleDateFormat(formatInput, Locale.getDefault())
    val outputFormat = SimpleDateFormat(formatOutput, Locale.getDefault())
    return try {
        val date = inputFormat.parse(this)
        Integer.parseInt(outputFormat.format(date ?: 0))
    } catch (e: Exception) {
        0
    }
}

// convert local time to utc
fun String.toDateUTC(
    formatInput: String = FORMAT_DATE_TIME_API,
    formatOutput: String = FORMAT_DATE_TIME_API
): String {
    if(this.isEmpty()) return ""
    val inputFormat = SimpleDateFormat(formatInput, Locale.getDefault())
    val outputFormat = SimpleDateFormat(formatOutput, Locale.getDefault())
    outputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val dateInput = inputFormat.parse(this)
    return outputFormat.format(dateInput ?: getDateCurrent(formatOutput))
}

// convert Utc to local time
fun String.convertUTCToLocal(
    formatInput: String = FORMAT_DATE_TIME_API,
    formatOutput: String = FORMAT_DATE_TIME_API
): String {
    if(this.isEmpty()) return ""
    val inputFormat = SimpleDateFormat(formatInput, Locale.getDefault())
    val outputFormat = SimpleDateFormat(formatOutput, Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val dateInput = inputFormat.parse(this)
    return outputFormat.format(dateInput ?: getDateCurrent(formatOutput))
}

fun getDateCurrent(format: String = FORMAT_DATE_TIME_API): String {
    val timeCurrent = Calendar.getInstance().time
    val outputFormat = SimpleDateFormat(format, Locale.getDefault())
    return outputFormat.format(timeCurrent)
}

fun String.convertTime(oldFormat: String,newFormat : String): String {
    val inputFormat = SimpleDateFormat(oldFormat, Locale.US)
    val outputFormat = SimpleDateFormat(newFormat, Locale.US)
    val date = inputFormat.parse(this)
    return outputFormat.format(date)
}

@SuppressLint("SimpleDateFormat")
fun convertTimestampToFormattedTime(timestamp: String?): String {
    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
    inputDateFormat.timeZone = TimeZone.getTimeZone("UTC")

    val outputDateFormat = SimpleDateFormat("hh:mm a")
    outputDateFormat.timeZone = TimeZone.getDefault()

    return try {
        val date = inputDateFormat.parse(timestamp)
        outputDateFormat.format(date.time)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
