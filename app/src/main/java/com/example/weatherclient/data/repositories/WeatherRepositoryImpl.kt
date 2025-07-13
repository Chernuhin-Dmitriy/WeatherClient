package com.example.weatherclient.data.repositories

import com.example.weatherclient.data.local.database.dao.WeatherDao
import com.example.weatherclient.data.mappers.WeatherMapper
import com.example.weatherclient.data.remote.api.WeatherApi
import com.example.weatherclient.domain.entities.WeatherWithForecast
import com.example.weatherclient.domain.exceptions.WeatherException
import com.example.weatherclient.domain.repositories.WeatherRepository
import com.example.weatherclient.domain.util.Result
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao
    // Удалите параметр apiKey из конструктора
) : WeatherRepository {

    companion object {
        private const val CACHE_EXPIRY_MS = 30 * 60 * 1000 // 30 minutes
    }

    override suspend fun getWeatherByCity(cityName: String): Result<WeatherWithForecast> {
        return try {
            // Check cache first
            val cachedWeather = weatherDao.getWeatherByCity(cityName)
            val cachedForecast = weatherDao.getForecastByCity(cityName)

            if (cachedWeather != null && isCacheValid(cachedWeather.cacheTimestamp)) {
                val weatherWithForecast = WeatherMapper.mapFromEntities(cachedWeather, cachedForecast)
                return Result.Success(weatherWithForecast)
            }

            // Fetch from API - убрать apiKey из вызова
            val response = weatherApi.getWeatherForecast(cityName)
            val weatherWithForecast = WeatherMapper.mapToWeatherWithForecast(response)

            // Cache the result
            val (weatherEntity, forecastEntities) = WeatherMapper.mapToEntities(weatherWithForecast)
            weatherDao.insertWeatherWithForecast(weatherEntity, forecastEntities)

            Result.Success(weatherWithForecast)
        } catch (e: Exception) {
            // Return cached data if available, even if expired
            val cachedWeather = weatherDao.getWeatherByCity(cityName)
            val cachedForecast = weatherDao.getForecastByCity(cityName)

            if (cachedWeather != null) {
                val weatherWithForecast = WeatherMapper.mapFromEntities(cachedWeather, cachedForecast)
                Result.Success(weatherWithForecast)
            } else {
                Result.Error(mapException(e))
            }
        }
    }

    override suspend fun getWeatherByCoordinates(lat: Double, lon: Double): Result<WeatherWithForecast> {
        return try {
            val location = "$lat,$lon"
            // Убрать apiKey из вызова
            val response = weatherApi.getWeatherForecast(location)
            val weatherWithForecast = WeatherMapper.mapToWeatherWithForecast(response)

            // Cache the result
            val (weatherEntity, forecastEntities) = WeatherMapper.mapToEntities(weatherWithForecast)
            weatherDao.insertWeatherWithForecast(weatherEntity, forecastEntities)

            Result.Success(weatherWithForecast)
        } catch (e: Exception) {
            Result.Error(mapException(e))
        }
    }

    override suspend fun refreshWeather(cityName: String): Result<WeatherWithForecast> {
        return try {
            // Убрать apiKey из вызова
            val response = weatherApi.getWeatherForecast(cityName)
            val weatherWithForecast = WeatherMapper.mapToWeatherWithForecast(response)

            // Cache the result
            val (weatherEntity, forecastEntities) = WeatherMapper.mapToEntities(weatherWithForecast)
            weatherDao.insertWeatherWithForecast(weatherEntity, forecastEntities)

            Result.Success(weatherWithForecast)
        } catch (e: Exception) {
            Result.Error(mapException(e))
        }
    }

    private fun isCacheValid(cacheTimestamp: Long): Boolean {
        return System.currentTimeMillis() - cacheTimestamp < CACHE_EXPIRY_MS
    }

    private fun mapException(exception: Exception): Exception {
        return when (exception) {
            is retrofit2.HttpException -> {
                when (exception.code()) {
                    400 -> WeatherException.CityNotFoundException("City not found")
                    401 -> WeatherException.ApiException("Invalid API key", exception.code())
                    403 -> WeatherException.ApiException("API key quota exceeded", exception.code())
                    else -> WeatherException.ApiException("API error: ${exception.message()}", exception.code())
                }
            }
            is java.net.UnknownHostException, is java.net.SocketTimeoutException -> {
                WeatherException.NetworkException("Network error: ${exception.message}")
            }
            else -> exception
        }
    }
}