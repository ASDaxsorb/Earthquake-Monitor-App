package com.axellsolis.earthquakemonitor.data.repository

import com.axellsolis.earthquakemonitor.data.model.Earthquake
import com.axellsolis.earthquakemonitor.data.model.EarthquakeResponse
import com.axellsolis.earthquakemonitor.data.network.EarthquakeApi
import com.axellsolis.earthquakemonitor.utils.Constants.STATUS_OK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EarthquakeRepository
@Inject
constructor(private val api: EarthquakeApi) {
    fun getAllEarthquakes(): Flow<List<Earthquake>> = flow {
        kotlin.runCatching {
            api.getAllDayEarthquakes()
        }.onSuccess { response ->
            if (response.metadata.status == STATUS_OK) {
                emit(response.earthquakes)
            }
        }
    }.flowOn(Dispatchers.IO)


    fun getAllHourEarthquakes(): Flow<List<Earthquake>> = flow {
        kotlin.runCatching {
            api.getAllHourEarthquakes()
        }.onSuccess { response ->
            if (response.metadata.status == STATUS_OK) {
                emit(response.earthquakes)
            }
        }
    }.flowOn(Dispatchers.IO)
}