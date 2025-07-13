package com.example.weatherclient.data.repositories

import com.example.weatherclient.data.local.database.dao.CityDao
import com.example.weatherclient.data.mappers.CityMapper
import com.example.weatherclient.data.remote.api.WeatherApi
import com.example.weatherclient.domain.entities.City
import com.example.weatherclient.domain.exceptions.WeatherException
import com.example.weatherclient.domain.repositories.CityRepository
import com.example.weatherclient.domain.util.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityRepositoryImpl @Inject constructor(
    private val cityDao: CityDao,
    private val weatherApi: WeatherApi
    // Удалите параметр apiKey из конструктора
) : CityRepository {

    override suspend fun getCities(): Result<List<City>> {
        return try {
            val cities = cityDao.getAllCities().map { CityMapper.mapFromEntity(it) }
            Result.Success(cities)
        } catch (e: Exception) {
            Result.Error(WeatherException.DatabaseException("Failed to get cities: ${e.message}"))
        }
    }

    override suspend fun addCity(cityName: String): Result<City> {
        return try {
            // Убрать apiKey из вызова
            val searchResults = weatherApi.searchCities(cityName)
            val cityResult = searchResults.firstOrNull {
                it.name.equals(cityName, ignoreCase = true)
            } ?: searchResults.firstOrNull()

            if (cityResult == null) {
                return Result.Error(WeatherException.CityNotFoundException(cityName))
            }

            // Check if city already exists
            val existingCity = cityDao.getCityByNameAndCountry(cityResult.name, cityResult.country)
            if (existingCity != null) {
                return Result.Error(WeatherException.CityAlreadyExistsException(cityResult.name))
            }

            val city = CityMapper.mapFromSearchResponse(cityResult)
            val cityEntity = CityMapper.mapToEntity(city)

            // If this is the first city, make it default
            val allCities = cityDao.getAllCities()
            val shouldBeDefault = allCities.isEmpty()

            val cityId = cityDao.insertCity(cityEntity.copy(isDefault = shouldBeDefault))

            Result.Success(city.copy(id = cityId, isDefault = shouldBeDefault))
        } catch (e: Exception) {
            Result.Error(mapException(e))
        }
    }

    override suspend fun removeCity(cityId: Long): Result<Unit> {
        return try {
            cityDao.deleteCity(cityId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(WeatherException.DatabaseException("Failed to remove city: ${e.message}"))
        }
    }

    override suspend fun setDefaultCity(cityId: Long): Result<Unit> {
        return try {
            cityDao.updateDefaultCity(cityId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(WeatherException.DatabaseException("Failed to set default city: ${e.message}"))
        }
    }

    override suspend fun getDefaultCity(): Result<City?> {
        return try {
            val defaultCity = cityDao.getDefaultCity()?.let { CityMapper.mapFromEntity(it) }
            Result.Success(defaultCity)
        } catch (e: Exception) {
            Result.Error(WeatherException.DatabaseException("Failed to get default city: ${e.message}"))
        }
    }

    override suspend fun searchCities(query: String): Result<List<City>> {
        return try {
            // Убрать apiKey из вызова
            val searchResults = weatherApi.searchCities(query)
            val cities = searchResults.map { CityMapper.mapFromSearchResponse(it) }
            Result.Success(cities)
        } catch (e: Exception) {
            Result.Error(mapException(e))
        }
    }

    private fun mapException(exception: Exception): Exception {
        return when (exception) {
            is retrofit2.HttpException -> {
                val errorCode = exception.code()
                val errorMsg = when (errorCode) {
                    400 -> "Invalid request (check query parameters)"
                    401 -> "Invalid API key"
                    404 -> "City not found"
                    429 -> "Too many requests (rate limit exceeded)"
                    500 -> "Server error"
                    else -> "API error: ${exception.message()}"
                }
                WeatherException.ApiException(errorMsg, errorCode)
            }
            is java.net.UnknownHostException, is java.net.SocketTimeoutException -> {
                WeatherException.NetworkException("Network error: ${exception.message}")
            }
            else -> exception
        }
    }
}