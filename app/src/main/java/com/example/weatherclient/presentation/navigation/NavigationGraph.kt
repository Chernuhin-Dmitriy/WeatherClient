package com.example.weatherclient.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherclient.presentation.ui.screens.cities.CitiesScreen
import com.example.weatherclient.presentation.ui.screens.main.MainScreen
import com.example.weatherclient.presentation.ui.screens.weather.WeatherScreen

@Composable
fun WeatherNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToCities = {
                    navController.navigate(Screen.Cities.route)
                },
                onNavigateToWeather = { cityName ->
                    navController.navigate(Screen.Weather.createRoute(cityName))
                }
            )
        }

        composable(Screen.Cities.route) {
            CitiesScreen(
                onNavigateToWeather = { cityName ->
                    navController.navigate(Screen.Weather.createRoute(cityName))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Weather.route,
            arguments = Screen.Weather.arguments
        ) { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
            WeatherScreen(
                cityName = cityName,
                onNavigateBack = {
                    // Проверяем, есть ли Cities в стеке
                    if (navController.previousBackStackEntry?.destination?.route == Screen.Cities.route) {
                        navController.popBackStack()
                    } else {
                        // Если Cities нет в стеке, навигируем к нему
                        navController.navigate(Screen.Cities.route) {
                            popUpTo(Screen.Main.route) {
                                inclusive = false
                            }
                        }
                    }
                }
            )
        }
    }
}

