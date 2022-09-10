package com.alexyach.kotlin.weatherapi.repository.local

import com.alexyach.kotlin.weatherapi.model.WeatherModel

interface IWeatherRoom {
    fun getWeatherAll(callback : (List<WeatherModel>) -> Unit)
    fun saveWeatherToRoom(weather: WeatherModel)
    fun updateForCityName(weather: WeatherModel, responseInt: (Int) -> Unit)
    fun deleteAll()
}
