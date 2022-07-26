package com.alexyach.kotlin.weatherapi.repository

fun interface IRepositoryWeatherDetails {
    fun getWeatherDetailsByCityName(cityName: String, callbackResponse: ICallbackResponse)
}