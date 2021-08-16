package com.axellsolis.earthquakemonitor.utils

import com.axellsolis.earthquakemonitor.data.model.Earthquake

interface ItemClickListener {
    fun onClick(earthquake: Earthquake)
    fun onLongClick(earthquake: Earthquake)
}