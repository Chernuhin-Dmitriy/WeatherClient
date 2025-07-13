package com.example.weatherclient.data.remote.api

import com.example.weatherclient.data.remote.dto.SearchResponse
import com.example.weatherclient.data.remote.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast.json")
    suspend fun getWeatherForecast(
        @Query("q") location: String,
        @Query("days") days: Int = 5
    ): WeatherResponse //WeatherForecastResponse

    @GET("search.json")
    suspend fun searchCities(
        @Query("q") query: String
    ): List<SearchResponse> //CitySearchResponse
}