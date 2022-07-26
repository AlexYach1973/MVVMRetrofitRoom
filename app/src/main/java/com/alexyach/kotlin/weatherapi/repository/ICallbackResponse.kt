package com.alexyach.kotlin.weatherapi.repository

import com.alexyach.kotlin.weatherapi.model.WeatherModel
import java.lang.Exception

interface ICallbackResponse {
    fun onCallbackResponse(callbackWeather: WeatherModel)
    fun onCallbackFailure(e: Exception)
}