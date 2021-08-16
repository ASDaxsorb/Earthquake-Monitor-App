package com.axellsolis.earthquakemonitor.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.axellsolis.earthquakemonitor.data.entity.EarthquakeEntity

@Database(entities = [EarthquakeEntity::class], version = 2)
abstract class EarthquakeDataBase : RoomDatabase() {

    abstract fun earthquakeDao(): EarthquakeDao

}