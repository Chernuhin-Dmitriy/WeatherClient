package com.example.weatherclient.presentation.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherclient.domain.entities.WeatherWithForecast
import com.example.weatherclient.domain.usecases.GetDefaultCityUseCase
import com.example.weatherclient.domain.usecases.GetWeatherUseCase
import com.example.weatherclient.domain.usecases.RefreshWeatherUseCase
import com.example.weatherclient.domain.util.onError
import com.example.weatherclient.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDefaultCityUseCase: GetDefaultCityUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val refreshWeatherUseCase: RefreshWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getDefaultCityUseCase().onSuccess { defaultCity ->
                if (defaultCity != null) {
                    loadWeatherForCity(defaultCity.name)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        shouldShowCityList = true
                    )
                }
            }.onError { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message
                )
            }
        }
    }

    fun navigateToCities() {
        _uiState.value = _uiState.value.copy(
            shouldShowCityList = true,
            hasNavigatedToWeather = false
        )
    }

    fun markAsNavigatedToWeather() {
        _uiState.value = _uiState.value.copy(hasNavigatedToWeather = true)
    }

    fun refreshWeather() {
        val currentCity = _uiState.value.weather?.weather?.city?.name
        if (currentCity != null) {
            refreshWeatherForCity(currentCity)
        } else {
            loadInitialData()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun loadWeatherForCity(cityName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getWeatherUseCase(cityName).onSuccess { weather ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    weather = weather,
                    shouldShowCityList = false,
                    hasNavigatedToWeather = false
                )
            }.onError { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message
                )
            }
        }
    }

    private fun refreshWeatherForCity(cityName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)

            refreshWeatherUseCase(cityName).onSuccess { weather ->
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    weather = weather
                )
            }.onError { error ->
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    errorMessage = error.message
                )
            }
        }
    }
}

data class MainUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val weather: WeatherWithForecast? = null,
    val shouldShowCityList: Boolean = false,
    val hasNavigatedToWeather: Boolean = false,
    val errorMessage: String? = null
)