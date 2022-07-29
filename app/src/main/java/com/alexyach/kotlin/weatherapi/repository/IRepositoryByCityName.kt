package com.alexyach.kotlin.weatherapi.repository

fun interface IRepositoryByCityName {
    fun getWeatherDetailsByCityName(cityName: String, callbackResponse: ICallbackResponse)
}