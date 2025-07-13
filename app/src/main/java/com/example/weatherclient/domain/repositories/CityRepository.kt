package com.example.weatherclient.domain.repositories

import com.example.weatherclient.domain.entities.City

interface CityRepository {
    suspend fun getCities(): com.example.weatherclient.domain.util.Result<List<City>>
    suspend fun addCity(cityName: String): com.example.weatherclient.domain.util.Result<City>
    suspend fun removeCity(cityId: Long): com.example.weatherclient.domain.util.Result<Unit>
    suspend fun setDefaultCity(cityId: Long): com.example.weatherclient.domain.util.Result<Unit>
    suspend fun getDefaultCity(): com.example.weatherclient.domain.util.Result<City?>
    suspend fun searchCities(query: String): com.example.weatherclient.domain.util.Result<List<City>>
}