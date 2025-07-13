package com.example.weatherclient.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weatherclient.data.local.database.entities.CityEntity

@Dao
interface CityDao {
    @Query("SELECT * FROM cities ORDER BY isDefault DESC, name ASC")
    suspend fun getAllCities(): List<CityEntity>

    @Query("SELECT * FROM cities WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultCity(): CityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity): Long

    @Query("DELETE FROM cities WHERE id = :cityId")
    suspend fun deleteCity(cityId: Long)

    @Transaction
    suspend fun updateDefaultCity(cityId: Long) {
        // First, remove default status from all cities
        clearDefaultCities()
        // Then set the new default city
        setDefaultCity(cityId)
    }

    @Query("UPDATE cities SET isDefault = 0")
    suspend fun clearDefaultCities()

    @Query("UPDATE cities SET isDefault = 1 WHERE id = :cityId")
    suspend fun setDefaultCity(cityId: Long)

    @Query("SELECT * FROM cities WHERE name = :name AND country = :country LIMIT 1")
    suspend fun getCityByNameAndCountry(name: String, country: String): CityEntity?
}