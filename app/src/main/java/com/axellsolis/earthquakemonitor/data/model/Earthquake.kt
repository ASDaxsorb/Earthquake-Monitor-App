package com.axellsolis.earthquakemonitor.data.model

import com.google.gson.annotations.SerializedName

data class Earthquake(
    @SerializedName("id")
    val id: String,
    @SerializedName("geometry")
    val geometry: Geometry,
    @SerializedName("properties")
    val properties: Properties,
)