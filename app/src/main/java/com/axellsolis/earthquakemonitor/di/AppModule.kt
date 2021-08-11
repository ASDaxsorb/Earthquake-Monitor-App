package com.axellsolis.earthquakemonitor.di

import android.content.Context
import androidx.room.Room
import com.axellsolis.earthquakemonitor.data.network.EarthquakeApi
import com.axellsolis.earthquakemonitor.db.EarthquakeDataBase
import com.axellsolis.earthquakemonitor.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesBaseUrl() = Constants.BASE_URL

    @Provides
    @Singleton
    fun providesRetrofit(BASE_URL: String): EarthquakeApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(EarthquakeApi::class.java)

    @Provides
    @Singleton
    fun providesDataBase(
        @ApplicationContext contextApp: Context
    ): EarthquakeDataBase = Room.databaseBuilder(
        contextApp,
        EarthquakeDataBase::class.java,
        "db_earthquake"
    ).build()

    @Provides
    @Singleton
    fun providesEarthquakeDao(db: EarthquakeDataBase) = db.earthquakeDao()
}