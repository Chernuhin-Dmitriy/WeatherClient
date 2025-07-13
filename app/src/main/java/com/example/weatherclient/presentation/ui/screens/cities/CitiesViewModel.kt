package com.example.weatherclient.presentation.ui.screens.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherclient.domain.usecases.AddCityUseCase
import com.example.weatherclient.domain.usecases.GetCitiesUseCase
import com.example.weatherclient.domain.usecases.RemoveCityUseCase
import com.example.weatherclient.domain.usecases.SearchCitiesUseCase
import com.example.weatherclient.domain.usecases.SetDefaultCityUseCase
import com.example.weatherclient.domain.util.onError
import com.example.weatherclient.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val addCityUseCase: AddCityUseCase,
    private val removeCityUseCase: RemoveCityUseCase,
    private val setDefaultCityUseCase: SetDefaultCityUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CitiesUiState())
    val uiState: StateFlow<CitiesUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadCities()
    }

    fun loadCities() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getCitiesUseCase().onSuccess { cities ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    cities = cities
                )
            }.onError { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message
                )
            }
        }
    }

    fun searchCities(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        // Cancel previous search job
        searchJob?.cancel()

        if (query.length >= 3) {
            searchJob = viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isSearching = true, searchResults = emptyList())

                searchCitiesUseCase(query).onSuccess { cities ->
                    _uiState.value = _uiState.value.copy(
                        isSearching = false,
                        searchResults = cities
                    )
                }.onError { error ->
                    _uiState.value = _uiState.value.copy(
                        isSearching = false,
                        searchResults = emptyList(),
                        errorMessage = error.message
                    )
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(
                isSearching = false,
                searchResults = emptyList()
            )
        }
    }

    fun addCity(cityName: String) {
        if (cityName.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "City name cannot be empty"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAddingCity = true)

            addCityUseCase(cityName).onSuccess { city ->
                _uiState.value = _uiState.value.copy(
                    isAddingCity = false,
                    searchQuery = "",
                    searchResults = emptyList()
                )
                loadCities() // Refresh the list
            }.onError { error ->
                _uiState.value = _uiState.value.copy(
                    isAddingCity = false,
                    errorMessage = error.message
                )
            }
        }
    }

    fun removeCity(cityId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            removeCityUseCase(cityId).onSuccess {
                loadCities() // Refresh the list
            }.onError { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message
                )
            }
        }
    }

    fun setDefaultCity(cityId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            setDefaultCityUseCase(cityId).onSuccess {
                loadCities() // Refresh the list
            }.onError { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message
                )
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            searchResults = emptyList(),
            isSearching = false
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}