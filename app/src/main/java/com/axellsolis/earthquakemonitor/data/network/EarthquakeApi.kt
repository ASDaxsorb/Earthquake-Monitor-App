package com.axellsolis.earthquakemonitor.data.network

import com.axellsolis.earthquakemonitor.data.model.EarthquakeResponse
import com.axellsolis.earthquakemonitor.utils.Constants
import retrofit2.http.GET

interface EarthquakeApi {

    @GET(Constants.ALL_DAY_END_POINT)
    suspend fun getAllDayEarthquakes(): EarthquakeResponse

    @GET(Constants.ALL_HOUR_END_POINT)
    suspend fun getAllHourEarthquakes(): EarthquakeResponse

}