package com.example.weatherclient.domain.repositories

import com.example.weatherclient.domain.entities.WeatherWithForecast

interface WeatherRepository {
    suspend fun getWeatherByCity(cityName: String): com.example.weatherclient.domain.util.Result<WeatherWithForecast>
    suspend fun getWeatherByCoordinates(lat: Double, lon: Double): com.example.weatherclient.domain.util.Result<WeatherWithForecast>
    suspend fun refreshWeather(cityName: String): com.example.weatherclient.domain.util.Result<WeatherWithForecast>
}