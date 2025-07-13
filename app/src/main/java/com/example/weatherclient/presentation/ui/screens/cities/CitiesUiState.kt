package com.example.weatherclient.presentation.ui.screens.cities

import com.example.weatherclient.domain.entities.City

data class CitiesUiState(
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val isAddingCity: Boolean = false,
    val cities: List<City> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<City> = emptyList(),
    val errorMessage: String? = null
)
