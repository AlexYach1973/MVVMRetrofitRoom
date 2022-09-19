package com.alexyach.kotlin.weatherapi.repository.local

import android.annotation.SuppressLint
import com.alexyach.kotlin.weatherapi.model.WeatherModel
import com.alexyach.kotlin.weatherapi.repository.ICallbackResponse
import com.alexyach.kotlin.weatherapi.repository.IRepositoryByCityName
import com.alexyach.kotlin.weatherapi.room.IWeatherDAO
import com.alexyach.kotlin.weatherapi.utils.toWeatherEntity
import com.alexyach.kotlin.weatherapi.utils.toWeatherModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

//@Singleton
class RepositoryRoomImpl @Inject constructor(
    private val weatherDao: IWeatherDAO
) : IRepositoryByCityName, IWeatherRoom {

    @SuppressLint("CheckResult")
    override fun getWeatherDetailsByCityName(
        cityName: String,
        callbackResponse: ICallbackResponse
    ) {

        // Room
        weatherDao.getWeatherByName(cityName)
            // Rx
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { weatherEntity ->
                    callbackResponse.onCallbackResponse(
                        weatherEntity.toWeatherModel()
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

//        Log.d("myLogs","MainActivity @Inject: $weatherDao")

        // Room
        weatherDao.getWeatherAll()
            // Rx
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { weatherEntity ->
                callback(weatherEntity.map {
                    it.toWeatherModel()
                })
            }
    }

    @SuppressLint("CheckResult")
    override fun saveWeatherToRoom(weather: WeatherModel) {

        // Room
        weatherDao.insertWeather(weather.toWeatherEntity())
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

        weatherDao.updateForCityName(currentDate, weather.cityName)
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
        weatherDao.deleteAll()
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