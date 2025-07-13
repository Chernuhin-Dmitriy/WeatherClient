package com.example.weatherclient.domain.entities

data class City(
    val id: Long = 0,
    val name: String,
    val country: String,
    val region: String,
    val lat: Double,
    val lon: Double,
    val isDefault: Boolean = false
) {
    val displayName: String
        get() = if (region.isNotBlank() && region != name) {
            "$name, $region, $country"
        } else {
            "$name, $country"
        }
}