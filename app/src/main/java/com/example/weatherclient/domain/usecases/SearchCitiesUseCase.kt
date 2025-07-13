package com.example.weatherclient.domain.usecases

import com.example.weatherclient.domain.entities.City
import com.example.weatherclient.domain.repositories.CityRepository
import com.example.weatherclient.domain.util.Result // Добавьте этот импорт
import javax.inject.Inject

class SearchCitiesUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {
    suspend operator fun invoke(query: String): Result<List<City>> {
        return try {
            if (query.length < 3) {
                Result.Success(emptyList())
            } else {
                cityRepository.searchCities(query.trim())
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}


