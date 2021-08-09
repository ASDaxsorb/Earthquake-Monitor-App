package com.axellsolis.earthquakemonitor.data.model

import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("coordinates")
    val coordinates: List<Double>,
)