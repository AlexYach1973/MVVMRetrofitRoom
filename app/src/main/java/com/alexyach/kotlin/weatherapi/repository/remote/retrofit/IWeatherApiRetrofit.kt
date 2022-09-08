package com.alexyach.kotlin.weatherapi.repository.remote.retrofit

import com.alexyach.kotlin.weatherapi.repository.remote.retrofit.weatherDTO.WeatherDTO
import com.alexyach.kotlin.weatherapi.utils.WEATHER_API_KEY_NAME
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IWeatherApiRetrofit {

    /** Rx */
    @GET("/data/2.5/weather")
    fun getWeatherByCityName(
        @Query(WEATHER_API_KEY_NAME) keyValue: String,
        @Query("q") nameCity: String
    ): Observable<Response<WeatherDTO>>

    @GET("/data/2.5/weather")
    fun getWeatherByLocation(
        @Query(WEATHER_API_KEY_NAME) keyValue: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Observable<Response<WeatherDTO>>

}