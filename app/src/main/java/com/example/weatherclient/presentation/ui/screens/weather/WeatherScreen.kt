package com.example.weatherclient.presentation.ui.screens.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.weatherclient.domain.entities.Forecast
import com.example.weatherclient.domain.entities.Weather
import com.example.weatherclient.presentation.util.toDegrees
import com.example.weatherclient.presentation.util.toFormattedDate
import com.example.weatherclient.presentation.util.toFormattedTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    cityName: String,
    viewModel: WeatherViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    val snackbarHostState = remember { SnackbarHostState() }



    LaunchedEffect(cityName) {
        viewModel.loadWeather(cityName)
    }

    LaunchedEffect(pullToRefreshState.isAnimating) {
        if (pullToRefreshState.isAnimating) {
            viewModel.refreshWeather()
        }
    }

    LaunchedEffect(uiState.isRefreshing) {
        if (!uiState.isRefreshing && pullToRefreshState.isAnimating) {
            pullToRefreshState.animateToHidden()
        }
    }

    // Handle messages
    uiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    uiState.successMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearSuccessMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.weather?.weather?.city?.name ?: cityName,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refreshWeather() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBack(uiState))
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.weather != null -> {
                    WeatherContent(
                        weather = uiState.weather!!,
                        isDefault = uiState.isDefault,
                        onSetDefault = { viewModel.setAsDefaultCity() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No weather data available",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherContent(
    weather: com.example.weatherclient.domain.entities.WeatherWithForecast,
    isDefault: Boolean,
    onSetDefault: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CurrentWeatherCard(weather = weather.weather)
        }

        item {
            WeatherDetailsCard(weather = weather.weather)
        }

        item {
            if (!isDefault) {
                Button(
                    onClick = onSetDefault,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Set as Default City")
                }
            }
        }

        item {
            Text(
                text = "Forecast on 5 days",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(weather.forecast) { forecast ->
                    ForecastCard(forecast = forecast)
                }
            }
        }
    }
}

@Composable
private fun CurrentWeatherCard(
    weather: Weather,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = weather.city.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )

            val locationDetails = weather.city.displayName.substringAfter(", ", "")
            if (locationDetails.isNotEmpty()) {
                Text(
                    text = locationDetails,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = if (weather.conditionIconUrl.startsWith("//")) {
                        "https:${weather.conditionIconUrl}"
                    } else {
                        weather.conditionIconUrl
                    },
                    contentDescription = weather.condition,
                    modifier = Modifier.size(130.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = weather.currentTemperature.toDegrees(),
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Feels like ${weather.feelsLike.toDegrees()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = weather.condition,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Updated: ${weather.lastUpdated.toFormattedTime()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun WeatherDetailsCard(
    weather: Weather,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherDetailItem(
                    label = "Humidity",
                    value = "${weather.humidity}%",
                    modifier = Modifier.weight(1f)
                )

                WeatherDetailItem(
                    label = "Pressure",
                    value = "${weather.pressure} mb",
                    modifier = Modifier.weight(1f)
                )

                WeatherDetailItem(
                    label = "Wind Speed",
                    value = "${weather.windSpeed} km/h",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun WeatherDetailItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ForecastCard(
    forecast: Forecast,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = forecast.date.toFormattedDate(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            AsyncImage(
                model = if (forecast.conditionIconUrl.startsWith("//")) {
                    "https:${forecast.conditionIconUrl}"
                } else {
                    forecast.conditionIconUrl
                },
                contentDescription = forecast.condition,
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = forecast.maxTemp.toDegrees(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = forecast.minTemp.toDegrees(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${forecast.chanceOfRain}% rain",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun gradientBack(uiState: WeatherUiState): Brush {
    val condition = uiState.weather?.weather?.condition.orEmpty().lowercase()

    val colors = remember(condition) {
        when {
            condition.contains("rain") || condition.contains("drizzle") -> listOf(
                Color(0xFF4A90E2),
                Color(0xFF7B68EE),
                Color(0xFF9370DB)
            )

            condition.contains("snow") -> listOf(
                Color(0xFF87CEEB),
                Color(0xFFB0E0E6),
                Color(0xFFF0F8FF)
            )

            condition.contains("cloud") -> listOf(
                Color(0xFF708090),
                Color(0xFF778899),
                Color(0xFFB0C4DE)
            )

            condition.contains("sun") || condition.contains("clear") -> listOf(
                Color(0xFF87CEEB),
                Color(0xFF98D8E8),
                Color(0xFFF0F8FF)
            )

            else -> listOf( // Default gradient
                Color(0xFF4A90E2),
                Color(0xFF5BA0F2),
                Color(0xFF6CB0FF)
            )
        }
    }
    return Brush.verticalGradient(colors)
}
