package com.alexyach.kotlin.weatherapi.repository.remote.retrofit

import com.alexyach.kotlin.weatherapi.repository.remote.retrofit.weatherDTO.WeatherDTO
import com.alexyach.kotlin.weatherapi.utils.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IWeatherApiRetrofit {

    /** Rx */
    @GET("/data/2.5/weather")
    fun getWeatherByCityName(
        @Query(WEATHER_API_KEY_NAME) keyValue: String,
        @Query("q") nameCity: String
    ): Observable<WeatherDTO>

    @GET("/data/2.5/weather")
    fun getWeatherByLocation(
        @Query(WEATHER_API_KEY_NAME) keyValue: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Observable<WeatherDTO>
    /** --- */

    /*
    @GET("/data/2.5/weather")
    fun getWeatherByCityName(
        @Query(WEATHER_API_KEY_NAME) keyValue: String,
        @Query("q") nameCity: String
    ): Call<WeatherDTO>

    @GET("/data/2.5/weather")
    fun getWeatherByLocation(
        @Query(WEATHER_API_KEY_NAME) keyValue: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<WeatherDTO>
    */

}