package com.example.weatherclient.presentation.util

import com.example.weatherclient.domain.exceptions.WeatherException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Double.toDegrees(): String = "${this.toInt()}°"

fun String.toFormattedDate(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        val date = inputFormat.parse(this)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        this
    }
}

fun Long.toFormattedTime(): String {
    return try {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.format(Date(this))
    } catch (e: Exception) {
        ""
    }
}

fun Exception.toUserMessage(): String {
    return when (this) {
        is WeatherException.NetworkException -> "No internet connection"
        is WeatherException.CityNotFoundException -> "City not found"
        is WeatherException.ApiException -> "Weather service unavailable"
        else -> message ?: "Something went wrong"
    }
}