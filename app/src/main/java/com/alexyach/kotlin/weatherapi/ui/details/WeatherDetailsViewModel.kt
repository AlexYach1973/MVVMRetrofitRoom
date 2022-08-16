package com.alexyach.kotlin.weatherapi.ui.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexyach.kotlin.weatherapi.WeatherApiApp.Companion.getWeatherApiApp
import com.alexyach.kotlin.weatherapi.model.WeatherModel
import com.alexyach.kotlin.weatherapi.repository.ICallbackResponse
import com.alexyach.kotlin.weatherapi.repository.IRepositoryByCityName
import com.alexyach.kotlin.weatherapi.repository.local.RepositoryRoomImpl
import com.alexyach.kotlin.weatherapi.repository.remote.retrofit.RepositoryRetrofitImpl
import com.alexyach.kotlin.weatherapi.utils.KEY_GET_WEATHER_BY

const val TAG = "myLogs"

class WeatherDetailsViewModel(
    private val weatherDetailsAppState: MutableLiveData<WeatherDetailsAppState> =
        MutableLiveData<WeatherDetailsAppState>()
) : ViewModel() {


    private lateinit var repository: IRepositoryByCityName

    // Test variable
    var testInt = 0

    /** LiveData */
    fun getWeatherDetailsAppState(): MutableLiveData<WeatherDetailsAppState> {
        return weatherDetailsAppState
    }

    fun getWeatherDetailsFromRepository(weather: WeatherModel, network: Boolean) {
        // Обираємо репозиторій
        choiceRepository(network)

        // Відображаємо іконку завантаження
        weatherDetailsAppState.value = WeatherDetailsAppState.Loading

        // Отримаємо дані
        if (weather.cityName == KEY_GET_WEATHER_BY) {
            getWeatherByLocation(weather)
        } else {
            getWeatherByCityName(weather, network)
        }
    }

    private fun getWeatherByLocation(weather: WeatherModel) {
        if (repository is RepositoryRetrofitImpl) {

            (repository as RepositoryRetrofitImpl).getWeatherDetailsByLocation(
                weather.lat,
                weather.lon,
                object : ICallbackResponse {

                    override fun onCallbackResponse(callbackWeather: WeatherModel) {
                        weatherDetailsAppState.postValue(
                            WeatherDetailsAppState
                                .SuccessGetDetailsWeather(callbackWeather)
                        )
                    }

                    override fun onCallbackFailure(e: java.lang.Exception) {
                        weatherDetailsAppState.postValue(
                            WeatherDetailsAppState
                                .ErrorGetDetailsWeather("Помилка: ${e.message}")
                        )
                    }
                })
        } else {

            weatherDetailsAppState.postValue(
                WeatherDetailsAppState
                    .ErrorGetDetailsWeather("Помилка: ${"По координатах тількі з інтернетом"}")
            )
            Log.d(TAG, "По координатах тількі з інтернетом")
        }
    }

    private fun getWeatherByCityName(weather: WeatherModel, network: Boolean) {
        repository.getWeatherDetailsByCityName(weather.cityName, object : ICallbackResponse {

            override fun onCallbackResponse(callbackWeather: WeatherModel) {
                weatherDetailsAppState.postValue(
                    WeatherDetailsAppState
                        .SuccessGetDetailsWeather(callbackWeather)
                )

                // Записуємо чи оновлюємо Room
                saveOrUpdateRoom(callbackWeather, network)
            }

            override fun onCallbackFailure(e: Exception) {
                weatherDetailsAppState.postValue(
                    WeatherDetailsAppState
                        .ErrorGetDetailsWeather("Помилка: ${e.message}")
                )
            }
        })

    }

    private fun choiceRepository(network: Boolean) {
        repository = if (network) {
            RepositoryRetrofitImpl()
        } else {
            RepositoryRoomImpl()
        }
    }

    fun saveOrUpdateRoom(weather: WeatherModel, network: Boolean) {
        if (!network) return

        RepositoryRoomImpl().getWeatherAll() {weathersFromRoom ->
            if ((weathersFromRoom.filter { it.cityName == weather.cityName }).isEmpty()) {

                // Записуємо
                RepositoryRoomImpl().saveWeatherToRoom(weather)
            } else {

                // Оновлюємо
                RepositoryRoomImpl().updateWeather(weather)
            }
        }
    }

}