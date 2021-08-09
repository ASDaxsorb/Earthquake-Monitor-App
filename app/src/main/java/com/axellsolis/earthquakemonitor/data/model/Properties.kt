package com.axellsolis.earthquakemonitor.data.model

import com.google.gson.annotations.SerializedName

data class Properties(
    @SerializedName("mag")
    val magnitude: Double,
    @SerializedName("place")
    val place: String,
    @SerializedName("time")
    val time: Long
)