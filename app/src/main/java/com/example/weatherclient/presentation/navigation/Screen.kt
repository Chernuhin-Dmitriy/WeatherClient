package com.example.weatherclient.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object Main : Screen("main")

    object Cities : Screen("cities")

    object Weather : Screen(
        route = "weather/{cityName}",
        arguments = listOf(
            navArgument("cityName") {
                type = NavType.StringType
            }
        )
    ) {
        fun createRoute(cityName: String) = "weather/${cityName}"
    }
}