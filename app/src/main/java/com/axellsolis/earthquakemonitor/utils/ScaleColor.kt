package com.axellsolis.earthquakemonitor.utils

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.axellsolis.earthquakemonitor.R

@ColorInt
fun getScaleColor(context: Context, magnitude: Double): Int {

    if (magnitude < 5.5) {
        return ContextCompat.getColor(context, R.color.green)
    }

    if (magnitude > 5.5 && magnitude < 6.1) {
        return ContextCompat.getColor(context, R.color.yellow)
    }

    if (magnitude > 6.1 && magnitude < 7.0) {
        return ContextCompat.getColor(context, R.color.orange)
    }

    return ContextCompat.getColor(context, R.color.red)
}