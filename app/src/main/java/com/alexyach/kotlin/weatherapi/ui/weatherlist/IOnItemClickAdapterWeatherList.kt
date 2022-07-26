package com.alexyach.kotlin.weatherapi.ui.weatherlist

import com.alexyach.kotlin.weatherapi.model.WeatherModel

fun interface IOnItemClickAdapterWeatherList {
    fun onItemClickAdapterWeatherList(weather: WeatherModel)
}