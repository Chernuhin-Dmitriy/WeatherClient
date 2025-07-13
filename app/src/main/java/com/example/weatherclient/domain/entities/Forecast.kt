package com.example.weatherclient.domain.entities

data class Forecast(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val condition: String,
    val conditionIconUrl: String,
    val chanceOfRain: Int,
    val avgHumidity: Int
)

data class WeatherWithForecast(
    val weather: Weather,
    val forecast: List<Forecast>
)