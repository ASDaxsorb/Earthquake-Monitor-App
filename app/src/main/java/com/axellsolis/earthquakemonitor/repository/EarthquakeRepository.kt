package com.axellsolis.earthquakemonitor.repository

import com.axellsolis.earthquakemonitor.data.model.Earthquake
import com.axellsolis.earthquakemonitor.data.model.EarthquakeResponse
import com.axellsolis.earthquakemonitor.data.network.EarthquakeApi
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
        }.onSuccess {
            if (it.metadata.status == 200) {
                emit(it.earthquakes)
            }
        }
    }.flowOn(Dispatchers.IO)
}