package com.example.weatherclient.domain.usecases

import com.example.weatherclient.domain.entities.City
import com.example.weatherclient.domain.repositories.CityRepository
import com.example.weatherclient.domain.util.Result
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {
    suspend operator fun invoke(): Result<List<City>> {
        return try {
            cityRepository.getCities()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}