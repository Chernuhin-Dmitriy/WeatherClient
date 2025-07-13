package com.example.weatherclient.data.mappers

import com.example.weatherclient.data.local.database.entities.CityEntity
import com.example.weatherclient.data.remote.dto.SearchResponse
import com.example.weatherclient.domain.entities.City

object CityMapper {
    fun mapFromEntity(entity: CityEntity): City {
        return City(
            id = entity.id,
            name = entity.name,
            country = entity.country,
            region = entity.region,
            lat = entity.lat,
            lon = entity.lon,
            isDefault = entity.isDefault
        )
    }

    fun mapToEntity(city: City): CityEntity {
        return CityEntity(
            id = city.id,
            name = city.name,
            country = city.country,
            region = city.region,
            lat = city.lat,
            lon = city.lon,
            isDefault = city.isDefault
        )
    }

    fun mapFromSearchResponse(response: SearchResponse): City {
        return City(
            name = response.name,
            country = response.country,
            region = response.region,
            lat = response.lat,
            lon = response.lon
        )
    }
}