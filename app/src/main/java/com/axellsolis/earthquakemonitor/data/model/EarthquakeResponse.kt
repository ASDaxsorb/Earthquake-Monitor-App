package com.axellsolis.earthquakemonitor.data.model

import com.google.gson.annotations.SerializedName

data class EarthquakeResponse(
    val metadata: Metadata,
    @SerializedName("features")
    val earthquakes: List<Earthquake>
)
