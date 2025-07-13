package com.example.weatherclient.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weatherclient.data.local.database.entities.ForecastEntity
import com.example.weatherclient.data.local.database.entities.WeatherEntity

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE cityName = :cityName")
    suspend fun getWeatherByCity(cityName: String): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("DELETE FROM weather WHERE cityName = :cityName")
    suspend fun deleteWeatherByCity(cityName: String)

    @Query("SELECT * FROM forecasts WHERE cityName = :cityName ORDER BY date")
    suspend fun getForecastByCity(cityName: String): List<ForecastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecasts(forecasts: List<ForecastEntity>)

    @Query("DELETE FROM forecasts WHERE cityName = :cityName")
    suspend fun deleteForecastByCity(cityName: String)

    @Transaction
    suspend fun insertWeatherWithForecast(weather: WeatherEntity, forecasts: List<ForecastEntity>) {
        insertWeather(weather)
        deleteForecastByCity(weather.cityName)
        insertForecasts(forecasts)
    }
}