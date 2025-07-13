package com.example.weatherclient.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "location") val location: LocationDto,
    @Json(name = "current") val current: CurrentWeatherDto,
    @Json(name = "forecast") val forecast: ForecastResponseDto
)

@JsonClass(generateAdapter = true)
data class LocationDto(
    @Json(name = "name") val name: String,
    @Json(name = "region") val region: String,
    @Json(name = "country") val country: String,
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double
)

@JsonClass(generateAdapter = true)
data class CurrentWeatherDto(
    @Json(name = "temp_c") val tempC: Double,
    @Json(name = "feelslike_c") val feelsLikeC: Double,
    @Json(name = "condition") val condition: ConditionDto,
    @Json(name = "wind_kph") val windKph: Double,
    @Json(name = "wind_dir") val windDir: String,
    @Json(name = "pressure_mb") val pressureMb: Double,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "vis_km") val visKm: Double,
    @Json(name = "last_updated_epoch") val lastUpdatedEpoch: Long
)

@JsonClass(generateAdapter = true)
data class ConditionDto(
    @Json(name = "text") val text: String,
    @Json(name = "icon") val icon: String
)

@JsonClass(generateAdapter = true)
data class ForecastResponseDto(
    @Json(name = "forecastday") val forecastDays: List<ForecastDayDto>
)

@JsonClass(generateAdapter = true)
data class ForecastDayDto(
    @Json(name = "date") val date: String,
    @Json(name = "day") val day: DayDto
)

@JsonClass(generateAdapter = true)
data class DayDto(
    @Json(name = "maxtemp_c") val maxTempC: Double,
    @Json(name = "mintemp_c") val minTempC: Double,
    @Json(name = "condition") val condition: ConditionDto,
    @Json(name = "daily_chance_of_rain") val chanceOfRain: Int,
    @Json(name = "avghumidity") val avgHumidity: Int
)

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "region") val region: String,
    @Json(name = "country") val country: String,
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double
)