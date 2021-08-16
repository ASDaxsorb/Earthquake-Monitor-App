package com.axellsolis.earthquakemonitor.db

import androidx.room.*
import com.axellsolis.earthquakemonitor.data.entity.EarthquakeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EarthquakeDao {

    @Query("SELECT * FROM earthquake ORDER BY magnitude DESC")
    fun getAllEarthquakes(): Flow<List<EarthquakeEntity>>

    @Query("SELECT * FROM earthquake WHERE id = :id")
    suspend fun getEarthquake(id: Int): EarthquakeEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveEarthquake(earthquakeEntity: EarthquakeEntity)

    @Delete
    suspend fun deleteItem(earthquakeEntity: EarthquakeEntity)
}