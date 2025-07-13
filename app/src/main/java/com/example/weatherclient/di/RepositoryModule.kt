package com.example.weatherclient.di

import com.example.weatherclient.data.repositories.CityRepositoryImpl
import com.example.weatherclient.data.repositories.WeatherRepositoryImpl
import com.example.weatherclient.domain.repositories.CityRepository
import com.example.weatherclient.domain.repositories.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    abstract fun bindCityRepository(
        cityRepositoryImpl: CityRepositoryImpl
    ): CityRepository
}