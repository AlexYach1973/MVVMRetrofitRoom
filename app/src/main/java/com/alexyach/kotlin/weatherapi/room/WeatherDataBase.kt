package com.alexyach.kotlin.weatherapi.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherEntity::class], version = 1)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun weatherDao(): IWeatherDAO
}