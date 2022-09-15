package com.alexyach.kotlin.weatherapi

import android.app.Application
import androidx.room.Room
import com.alexyach.kotlin.weatherapi.room.WeatherDataBase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApiApp : Application() {

}