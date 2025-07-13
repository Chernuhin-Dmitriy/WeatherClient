package com.example.weatherclient.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val cityName: String,
    val country: String,
    val region: String,
    val lat: Double,
    val lon: Double,
    val currentTemperature: Double,
    val feelsLike: Double,
    val condition: String,
    val conditionIconUrl: String,
    val humidity: Int,
    val windSpeed: Double,
    val windDirection: String,
    val pressure: Double,
    val visibility: Double,
    val lastUpdated: Long,
    val cacheTimestamp: Long = System.currentTimeMillis()
)
