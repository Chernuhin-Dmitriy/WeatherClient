package com.example.weatherclient.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "forecasts",
    foreignKeys = [ForeignKey(
        entity = WeatherEntity::class,
        parentColumns = ["cityName"],
        childColumns = ["cityName"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cityName: String,
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val condition: String,
    val conditionIconUrl: String,
    val chanceOfRain: Int,
    val avgHumidity: Int
)