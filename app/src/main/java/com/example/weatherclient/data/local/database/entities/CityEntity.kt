package com.example.weatherclient.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val country: String,
    val region: String,
    val lat: Double,
    val lon: Double,
    val isDefault: Boolean = false
)