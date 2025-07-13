package com.example.weatherclient.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherclient.data.local.database.dao.CityDao
import com.example.weatherclient.data.local.database.dao.WeatherDao
import com.example.weatherclient.data.local.database.entities.CityEntity
import com.example.weatherclient.data.local.database.entities.ForecastEntity
import com.example.weatherclient.data.local.database.entities.WeatherEntity

@Database(
    entities = [WeatherEntity::class, CityEntity::class, ForecastEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun cityDao(): CityDao
}