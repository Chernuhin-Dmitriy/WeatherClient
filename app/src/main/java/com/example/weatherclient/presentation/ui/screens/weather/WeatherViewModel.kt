package com.example.weatherclient.presentation.ui.screens.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherclient.domain.entities.WeatherWithForecast
import com.example.weatherclient.domain.usecases.GetCitiesUseCase
import com.example.weatherclient.domain.usecases.GetWeatherUseCase
import com.example.weatherclient.domain.usecases.RefreshWeatherUseCase
import com.example.weatherclient.domain.usecases.SetDefaultCityUseCase
import com.example.weatherclient.domain.util.onError
import com.example.weatherclient.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val refreshWeatherUseCase: RefreshWeatherUseCase,
    private val setDefaultCityUseCase: SetDefaultCityUseCase,
    private val getCitiesUseCase: GetCitiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun loadWeather(cityName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getWeatherUseCase(cityName).onSuccess { weather ->
                // Проверяем, является ли этот город по умолчанию
                checkIfDefaultCity(cityName)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    weather = weather
                )
            }.onError { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message
                )
            }
        }
    }

    private fun checkIfDefaultCity(cityName: String) {
        viewModelScope.launch {
            getCitiesUseCase().onSuccess { cities ->
                val city = cities.find { it.name == cityName }
                _uiState.value = _uiState.value.copy(
                    isDefault = city?.isDefault == true
                )
            }
        }
    }

    fun refreshWeather() {
        val cityName = _uiState.value.weather?.weather?.city?.name
        if (cityName != null) {
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

    fun setAsDefaultCity() {
        val weather = _uiState.value.weather
        if (weather != null) {
            viewModelScope.launch {
                // Find the city in the database to get its ID
                getCitiesUseCase().onSuccess { cities ->
                    val city = cities.find { it.name == weather.weather.city.name }
                    if (city != null) {
                        setDefaultCityUseCase(city.id).onSuccess {
                            _uiState.value = _uiState.value.copy(
                                isDefault = true,
                                successMessage = "City set as default!"
                            )
                        }.onError { error ->
                            _uiState.value = _uiState.value.copy(errorMessage = error.message)
                        }
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
}

data class WeatherUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val weather: WeatherWithForecast? = null,
    val isDefault: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)