package com.axellsolis.earthquakemonitor.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.axellsolis.earthquakemonitor.data.model.Earthquake
import com.axellsolis.earthquakemonitor.data.model.Geometry
import com.axellsolis.earthquakemonitor.data.model.Properties

@Entity(tableName = "earthquake")
data class EarthquakeEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "place")
    val place: String,
    @ColumnInfo(name = "magnitude")
    val magnitude: Double,
    @ColumnInfo(name = "time")
    val time: Long,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "depth")
    val depth: Double
) {
    fun toModel(): Earthquake {
        val coordinates: List<Double> = listOf(longitude, latitude, depth)
        val properties = Properties(magnitude, place, time)
        val geometry = Geometry(coordinates)

        return Earthquake(
            id.toString(),
            properties = properties,
            geometry = geometry
        )
    }
}
