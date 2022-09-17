package com.alexyach.kotlin.weatherapi.room

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface IWeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weatherEntity: WeatherEntity) : Single<Long>

    @Query("SELECT * FROM WeatherEntity")
    fun getWeatherAll(): Flowable<List<WeatherEntity>>

    @Query("SELECT * FROM WeatherEntity WHERE nameCity=:name")
    fun getWeatherByName(name: String): Single<WeatherEntity>

    @Update
    fun updateWeather(weatherEntity: WeatherEntity) : Single<Int> /*Completable*/

    // Видалити ВСЕ
    @Query("DELETE FROM WeatherEntity")
    fun deleteAll() : Completable

    // Оновити дату по імені міста
    @Query("UPDATE WeatherEntity SET date = :date WHERE nameCity =:nameCity")
    fun updateForCityName(date: String, nameCity: String): Single<Int>

}