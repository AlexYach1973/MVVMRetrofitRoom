package com.alexyach.kotlin.weatherapi

import android.app.Application
import androidx.room.Room
import com.alexyach.kotlin.weatherapi.room.WeatherDataBase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        weatherApiApp = this

    }

    companion object {
        // Context
        private var weatherApiApp: WeatherApiApp? = null
        fun getWeatherApiApp() = weatherApiApp!!

        // DB
        private var weatherDataBase: WeatherDataBase? = null
        fun getWeatherDataBase(): WeatherDataBase {
            if (weatherDataBase == null) {
                weatherDataBase = Room.databaseBuilder(
                    getWeatherApiApp(),
                    WeatherDataBase::class.java,
                    "RoomDataBase"
                ).build()
            }
            return weatherDataBase!!
        }
    }
}