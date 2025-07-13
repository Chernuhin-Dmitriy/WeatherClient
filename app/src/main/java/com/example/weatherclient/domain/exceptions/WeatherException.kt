package com.example.weatherclient.domain.exceptions

sealed class WeatherException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    class NetworkException(message: String) : WeatherException(message)

    class ApiException(message: String, val code: Int) : WeatherException(message)

    class DatabaseException(message: String) : WeatherException(message)

    class CityNotFoundException(cityName: String) : WeatherException("City '$cityName' not found")

    class CityAlreadyExistsException(cityName: String) : WeatherException("City '$cityName' already exists in your list")

    class WeatherDataException(message: String) : WeatherException(message)
}