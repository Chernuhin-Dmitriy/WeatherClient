package com.example.weatherclient.domain.usecases

import com.example.weatherclient.domain.repositories.CityRepository
import com.example.weatherclient.domain.util.Result // Добавьте этот импорт
import javax.inject.Inject

class RemoveCityUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {
    suspend operator fun invoke(cityId: Long): Result<Unit> {
        return try {
            cityRepository.removeCity(cityId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}