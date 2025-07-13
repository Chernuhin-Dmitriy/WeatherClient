package com.example.weatherclient.data.mappers

import com.example.weatherclient.data.local.database.entities.ForecastEntity
import com.example.weatherclient.data.local.database.entities.WeatherEntity
import com.example.weatherclient.data.remote.dto.WeatherResponse
import com.example.weatherclient.domain.entities.City
import com.example.weatherclient.domain.entities.Forecast
import com.example.weatherclient.domain.entities.Weather
import com.example.weatherclient.domain.entities.WeatherWithForecast

object WeatherMapper {
    fun mapToWeatherWithForecast(response: WeatherResponse): WeatherWithForecast {
        val city = City(
            name = response.location.name,
            country = response.location.country,
            region = response.location.region,
            lat = response.location.lat,
            lon = response.location.lon
        )

        val weather = Weather(
            city = city,
            currentTemperature = response.current.tempC,
            feelsLike = response.current.feelsLikeC,
            condition = response.current.condition.text,
            conditionIconUrl = "https:${response.current.condition.icon}",
            humidity = response.current.humidity,
            windSpeed = response.current.windKph,
            windDirection = response.current.windDir,
            pressure = response.current.pressureMb,
            visibility = response.current.visKm,
            lastUpdated = response.current.lastUpdatedEpoch * 1000
        )

        val forecasts = response.forecast.forecastDays.map { day ->
            Forecast(
                date = day.date,
                maxTemp = day.day.maxTempC,
                minTemp = day.day.minTempC,
                condition = day.day.condition.text,
                conditionIconUrl = "https:${day.day.condition.icon}",
                chanceOfRain = day.day.chanceOfRain,
                avgHumidity = day.day.avgHumidity
            )
        }

        return WeatherWithForecast(weather, forecasts)
    }

    fun mapToEntities(weatherWithForecast: WeatherWithForecast): Pair<WeatherEntity, List<ForecastEntity>> {
        val weather = weatherWithForecast.weather
        val weatherEntity = WeatherEntity(
            cityName = weather.city.name,
            country = weather.city.country,
            region = weather.city.region,
            lat = weather.city.lat,
            lon = weather.city.lon,
            currentTemperature = weather.currentTemperature,
            feelsLike = weather.feelsLike,
            condition = weather.condition,
            conditionIconUrl = weather.conditionIconUrl,
            humidity = weather.humidity,
            windSpeed = weather.windSpeed,
            windDirection = weather.windDirection,
            pressure = weather.pressure,
            visibility = weather.visibility,
            lastUpdated = weather.lastUpdated
        )

        val forecastEntities = weatherWithForecast.forecast.map { forecast ->
            ForecastEntity(
                cityName = weather.city.name,
                date = forecast.date,
                maxTemp = forecast.maxTemp,
                minTemp = forecast.minTemp,
                condition = forecast.condition,
                conditionIconUrl = forecast.conditionIconUrl,
                chanceOfRain = forecast.chanceOfRain,
                avgHumidity = forecast.avgHumidity
            )
        }

        return weatherEntity to forecastEntities
    }

    fun mapFromEntities(weatherEntity: WeatherEntity, forecastEntities: List<ForecastEntity>): WeatherWithForecast {
        val city = City(
            name = weatherEntity.cityName,
            country = weatherEntity.country,
            region = weatherEntity.region,
            lat = weatherEntity.lat,
            lon = weatherEntity.lon
        )

        val weather = Weather(
            city = city,
            currentTemperature = weatherEntity.currentTemperature,
            feelsLike = weatherEntity.feelsLike,
            condition = weatherEntity.condition,
            conditionIconUrl = weatherEntity.conditionIconUrl,
            humidity = weatherEntity.humidity,
            windSpeed = weatherEntity.windSpeed,
            windDirection = weatherEntity.windDirection,
            pressure = weatherEntity.pressure,
            visibility = weatherEntity.visibility,
            lastUpdated = weatherEntity.lastUpdated
        )

        val forecasts = forecastEntities.map { entity ->
            Forecast(
                date = entity.date,
                maxTemp = entity.maxTemp,
                minTemp = entity.minTemp,
                condition = entity.condition,
                conditionIconUrl = entity.conditionIconUrl,
                chanceOfRain = entity.chanceOfRain,
                avgHumidity = entity.avgHumidity
            )
        }

        return WeatherWithForecast(weather, forecasts)
    }
}