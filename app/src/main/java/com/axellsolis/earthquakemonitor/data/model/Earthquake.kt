package com.axellsolis.earthquakemonitor.data.model

import com.axellsolis.earthquakemonitor.data.entity.EarthquakeEntity
import com.google.gson.annotations.SerializedName

data class Earthquake(
    @SerializedName("id")
    val id: String,
    @SerializedName("geometry")
    val geometry: Geometry,
    @SerializedName("properties")
    val properties: Properties,
) {
    fun toEntity(): EarthquakeEntity = EarthquakeEntity(
        place = properties.place,
        magnitude = properties.magnitude,
        time = properties.time,
        latitude = geometry.coordinates[1],
        longitude = geometry.coordinates[0],
        depth = geometry.coordinates[2]
    )
}