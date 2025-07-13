package com.example.weatherclient.domain.usecases

import com.example.weatherclient.domain.entities.City
import com.example.weatherclient.domain.exceptions.WeatherException
import com.example.weatherclient.domain.repositories.CityRepository
import com.example.weatherclient.domain.util.Result
import javax.inject.Inject

class AddCityUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {
    suspend operator fun invoke(cityName: String): Result<City> {
        return try {
            if (cityName.isBlank()) {
                Result.Error(WeatherException.CityNotFoundException(cityName))
            } else {
                cityRepository.addCity(cityName.trim())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}