package com.axellsolis.earthquakemonitor.data.repository

import com.axellsolis.earthquakemonitor.data.entity.EarthquakeEntity
import com.axellsolis.earthquakemonitor.db.EarthquakeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository
@Inject
constructor(private val dao: EarthquakeDao) {

    fun getEarthquakesEntity(): Flow<List<EarthquakeEntity>> =
        dao.getAllEarthquakes().flowOn(Dispatchers.IO)

    suspend fun saveEarthquake(earthquakeEntity: EarthquakeEntity) {
        dao.saveEarthquake(earthquakeEntity)
    }

}