package com.alexyach.kotlin.weatherapi.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexyach.kotlin.weatherapi.model.WeatherModel
import com.alexyach.kotlin.weatherapi.repository.ICallbackResponse
import com.alexyach.kotlin.weatherapi.repository.IRepositoryWeatherDetails
import com.alexyach.kotlin.weatherapi.repository.local.IResponseRoomCallback
import com.alexyach.kotlin.weatherapi.repository.local.RepositoryRoomImpl
import com.alexyach.kotlin.weatherapi.repository.remote.retrofit.RepositoryRetrofitImpl

class WeatherDetailsViewModel(
    private val weatherDetailsAppState: MutableLiveData<WeatherDetailsAppState> =
        MutableLiveData<WeatherDetailsAppState>()
) : ViewModel() {

    private lateinit var repository: IRepositoryWeatherDetails

    /** LiveData */
    fun getWeatherDetailsAppState(): MutableLiveData<WeatherDetailsAppState> {
        return weatherDetailsAppState
    }

    fun getWeatherDetailsFromRepository(cityName: String, network: Boolean) {
        // Обираємо репозиторій
        choiceRepository(network)
        // Відображаємо іконку завантаження
        weatherDetailsAppState.value = WeatherDetailsAppState.Loading
        // Отримаємо дані
        repository.getWeatherDetailsByCityName(cityName, object : ICallbackResponse {
            override fun onCallbackResponse(callbackWeather: WeatherModel) {
                weatherDetailsAppState.postValue(WeatherDetailsAppState
                    .SuccessGetDetailsWeather(callbackWeather))

                // Записуємо чи оновлюємо Room
                saveOrUpdateRoom(callbackWeather, network)
            }

            override fun onCallbackFailure(e: Exception) {
                weatherDetailsAppState.postValue(WeatherDetailsAppState
                    .ErrorGetDetailsWeather("Помилка: ${e.message}"))
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

    private fun saveOrUpdateRoom(weather: WeatherModel, network: Boolean) {
        if (!network) return

        RepositoryRoomImpl().getWeatherAll(object : IResponseRoomCallback {
            override fun onResponse(weathers: List<WeatherModel>) {

                if ((weathers.filter { it.cityName == weather.cityName }).isEmpty()) {
                    // Записуємо
                    RepositoryRoomImpl().saveWeatherToRoom(weather)
                } else {
                    // Оновлюємо
                    RepositoryRoomImpl().updateWeather(weather)
                }
            }
        })
    }

}