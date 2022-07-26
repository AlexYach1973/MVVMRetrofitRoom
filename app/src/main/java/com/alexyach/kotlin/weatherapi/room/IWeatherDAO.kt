package com.alexyach.kotlin.weatherapi.room

import androidx.room.*

@Dao
interface IWeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weatherEntity: WeatherEntity)

    @Query("SELECT * FROM WeatherEntity")
    fun getWeatherAll(): List<WeatherEntity>

    @Query("SELECT * FROM WeatherEntity WHERE nameCity=:name")
    fun getWeatherByName(name: String): WeatherEntity

    @Update
    fun updateWeather(weatherEntity: WeatherEntity)

    // Удалить ВСЕ
    @Query("DELETE FROM WeatherEntity")
    fun deleteAll()

}