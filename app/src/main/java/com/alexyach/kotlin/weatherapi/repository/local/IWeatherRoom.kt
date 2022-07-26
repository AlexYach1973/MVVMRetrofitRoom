package com.alexyach.kotlin.weatherapi.repository.local

import com.alexyach.kotlin.weatherapi.model.WeatherModel
import com.alexyach.kotlin.weatherapi.model.weatherDTO.Weather
import com.alexyach.kotlin.weatherapi.model.weatherDTO.WeatherDTO

interface IWeatherRoom {
    fun getWeatherAll(callback : IResponseRoomCallback)
    fun saveWeatherToRoom(weather: WeatherModel)
    fun updateWeather(weather: WeatherModel)
    fun deleteAll()
}

interface IResponseRoomCallback {
    fun onResponse(weathers: List<WeatherModel>)
}