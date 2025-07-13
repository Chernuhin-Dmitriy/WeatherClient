package com.example.weatherclient.di

import android.content.Context
import androidx.room.Room
import com.example.weatherclient.data.local.database.WeatherDatabase
import com.example.weatherclient.data.local.database.dao.CityDao
import com.example.weatherclient.data.local.database.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao {
        return database.weatherDao()
    }

    @Provides
    fun provideCityDao(database: WeatherDatabase): CityDao {
        return database.cityDao()
    }
}

