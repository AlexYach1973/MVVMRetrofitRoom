package com.alexyach.kotlin.weatherapi.repository

interface IRepositoryByLocation {
    fun getWeatherDetailsByLocation(lat: Double, lon: Double, callbackResponse: ICallbackResponse)
}