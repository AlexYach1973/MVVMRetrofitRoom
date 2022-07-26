package com.alexyach.kotlin.weatherapi.ui.details

import com.alexyach.kotlin.weatherapi.model.WeatherModel

sealed class WeatherDetailsAppState {
    data class SuccessGetDetailsWeather(val weather: WeatherModel): WeatherDetailsAppState()
    data class ErrorGetDetailsWeather(val errorString: String): WeatherDetailsAppState()

    object Loading: WeatherDetailsAppState()
}