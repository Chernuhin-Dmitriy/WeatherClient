package com.example.weatherclient.domain.entities

data class Weather(
    val city: City,
    val currentTemperature: Double,
    val feelsLike: Double,
    val condition: String,
    val conditionIconUrl: String,
    val humidity: Int,
    val windSpeed: Double,
    val windDirection: String,
    val pressure: Double,
    val visibility: Double,
    val lastUpdated: Long
)