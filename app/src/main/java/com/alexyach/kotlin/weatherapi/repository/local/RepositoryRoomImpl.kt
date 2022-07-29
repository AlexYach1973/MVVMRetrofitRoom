package com.alexyach.kotlin.weatherapi.repository.local

import com.alexyach.kotlin.weatherapi.WeatherApiApp
import com.alexyach.kotlin.weatherapi.model.WeatherModel
import com.alexyach.kotlin.weatherapi.repository.ICallbackResponse
import com.alexyach.kotlin.weatherapi.repository.IRepositoryByCityName
import com.alexyach.kotlin.weatherapi.room.WeatherEntity
import com.alexyach.kotlin.weatherapi.utils.converterWeatherEntityToWeatherModel
import com.alexyach.kotlin.weatherapi.utils.converterWeatherModelToWeatherEntity
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RepositoryRoomImpl : IRepositoryByCityName, IWeatherRoom {
    override fun getWeatherDetailsByCityName(
        cityName: String,
        callbackResponse: ICallbackResponse
    ) {

        Thread {

            val weatherEntity: WeatherEntity = WeatherApiApp.getWeatherDataBase().weatherDao()
                .getWeatherByName(cityName)

            if (weatherEntity != null) {
                callbackResponse.onCallbackResponse(
                    converterWeatherEntityToWeatherModel(weatherEntity)
                )
            } else {
                callbackResponse.onCallbackFailure(IOException("Нема такого міста в локальній базі"))
            }
        }.start()
    }

    override fun getWeatherAll(callback: IResponseRoomCallback) {

        Thread {
            val weatherEntity = WeatherApiApp.getWeatherDataBase().weatherDao().getWeatherAll()
            callback.onResponse(weatherEntity.map {
                converterWeatherEntityToWeatherModel(it)
            })
        }.start()
    }

    override fun saveWeatherToRoom(weather: WeatherModel) {
        val currentDate = SimpleDateFormat("dd.M.yy hh:mm:ss", Locale.ENGLISH).format(Date())
        val weatherEntity = converterWeatherModelToWeatherEntity(weather).apply {
            date = currentDate
        }

//        Thread {
            WeatherApiApp.getWeatherDataBase().weatherDao().insertWeather(weatherEntity)
//        }.start()


    }

    override fun updateWeather(weather: WeatherModel) {
//        Thread {
            WeatherApiApp.getWeatherDataBase().weatherDao().updateWeather(
                converterWeatherModelToWeatherEntity(weather)
            )
//        }.start()
    }

    override fun deleteAll() {
        Thread {
            WeatherApiApp.getWeatherDataBase().weatherDao().deleteAll()
        }.start()
    }

}