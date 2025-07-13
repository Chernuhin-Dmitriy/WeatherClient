package com.example.weatherclient.presentation.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.safe.args.generator.ErrorMessage

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToCities: () -> Unit,
    onNavigateToWeather: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.shouldShowCityList) {
        if (uiState.shouldShowCityList) {
            onNavigateToCities()
        }
    }

    LaunchedEffect(uiState.weather, uiState.hasNavigatedToWeather) {
        uiState.weather?.let { weather ->
            if (!uiState.hasNavigatedToWeather) {
                onNavigateToWeather(weather.weather.city.name)
                viewModel.markAsNavigatedToWeather()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.errorMessage != null -> {   // Без ErrorMessage пока
                AlertDialog(
                    onDismissRequest = { viewModel.clearError() },
                    title = { Text("Ошибка") },
                    text = { Text(uiState.errorMessage!!) },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.loadInitialData()
                            viewModel.clearError()
                        }) {
                            Text("Повторить")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("Закрыть")
                        }
                    }
                )
            }
        }
    }
}