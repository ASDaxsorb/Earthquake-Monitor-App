package com.axellsolis.earthquakemonitor.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun longToDate(time: Long): String {
    val date = Date(time)
    val formatter = SimpleDateFormat.getDateInstance()
    return formatter.format(date)
}

fun longToTime(time: Long): String {
    val date = Date(time)
    val formatter = SimpleDateFormat.getTimeInstance(DateFormat.LONG)
    return formatter.format(date)
}