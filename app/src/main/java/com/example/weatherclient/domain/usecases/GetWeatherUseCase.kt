package com.example.weatherclient.domain.usecases

import com.example.weatherclient.domain.entities.WeatherWithForecast
import com.example.weatherclient.domain.repositories.WeatherRepository
import com.example.weatherclient.domain.util.Result
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String): Result<WeatherWithForecast> {
        return try {
            weatherRepository.getWeatherByCity(cityName)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}