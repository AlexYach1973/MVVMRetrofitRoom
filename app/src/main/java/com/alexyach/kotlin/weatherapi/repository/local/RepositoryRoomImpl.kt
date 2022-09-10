package com.alexyach.kotlin.weatherapi.repository.local

import android.annotation.SuppressLint
import android.util.Log
import com.alexyach.kotlin.weatherapi.WeatherApiApp
import com.alexyach.kotlin.weatherapi.model.WeatherModel
import com.alexyach.kotlin.weatherapi.repository.ICallbackResponse
import com.alexyach.kotlin.weatherapi.repository.IRepositoryByCityName
import com.alexyach.kotlin.weatherapi.utils.converterWeatherEntityToWeatherModel
import com.alexyach.kotlin.weatherapi.utils.converterWeatherModelToWeatherEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RepositoryRoomImpl : IRepositoryByCityName, IWeatherRoom {

    @SuppressLint("CheckResult")
    override fun getWeatherDetailsByCityName(
        cityName: String,
        callbackResponse: ICallbackResponse
    ) {

        // Room
        WeatherApiApp.getWeatherDataBase().weatherDao().getWeatherByName(cityName)
            // Rx
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { weatherEntity ->
                    callbackResponse.onCallbackResponse(
                        converterWeatherEntityToWeatherModel(weatherEntity)
                    )
                },
                {
                    callbackResponse
                        .onCallbackFailure(IOException("Нема такого міста в локальній базі"))
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun getWeatherAll(callback: (List<WeatherModel>) -> Unit) {

        // Room
        WeatherApiApp.getWeatherDataBase().weatherDao().getWeatherAll()
            // Rx
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { weatherEntity ->
                callback(weatherEntity.map {
                    converterWeatherEntityToWeatherModel(it)
                })
            }
    }

    @SuppressLint("CheckResult")
    override fun saveWeatherToRoom(weather: WeatherModel) {
        val currentDate = SimpleDateFormat("dd MMM yyyy hh:mm:ss", Locale.ENGLISH).format(Date())
        val weatherEntity = converterWeatherModelToWeatherEntity(weather).apply {
            date = currentDate
        }
        // Room
        WeatherApiApp.getWeatherDataBase().weatherDao().insertWeather(weatherEntity)
            // Rx
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
//                Log.d("myLogs", "saveWeatherToRoom: ${weather.cityName}, Int =$it Ok!")
            }, {
//                Log.d("myLogs", "saveWeatherToRoom: ${weather.cityName} Error")
            })

    }

    @SuppressLint("CheckResult")
    override fun updateForCityName(weather: WeatherModel, responseInt: (Int) -> Unit) {
        val currentDate = SimpleDateFormat("dd MMM yyyy hh:mm:ss", Locale.ENGLISH).format(Date())

        WeatherApiApp.getWeatherDataBase().weatherDao()
            .updateForCityName(currentDate, weather.cityName)
            // Rx
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseInt(it)
//                Log.d("myLogs", "updateForCityName: ${weather.cityName}, Int =$it Ok!")
            }, {

//                Log.d("myLogs", "updateForCityName: ${weather.cityName} Error")
            })
    }

    @SuppressLint("CheckResult")
    override fun deleteAll() {
        // Room
        WeatherApiApp.getWeatherDataBase().weatherDao().deleteAll()
            // Rx
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
//                Log.d("myLogs", "deleteAll: Ok!")
            }, {
//                Log.d("myLogs", "deleteAll: Error")
            })
    }
}