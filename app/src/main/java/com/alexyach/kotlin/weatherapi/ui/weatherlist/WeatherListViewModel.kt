package com.alexyach.kotlin.weatherapi.ui.weatherlist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexyach.kotlin.weatherapi.model.WeatherModel
import com.alexyach.kotlin.weatherapi.model.getWorldCities
import com.alexyach.kotlin.weatherapi.repository.ICallbackResponse
import com.alexyach.kotlin.weatherapi.repository.local.RepositoryRoomImpl
import com.alexyach.kotlin.weatherapi.repository.remote.retrofit.RepositoryRetrofitImpl
import com.alexyach.kotlin.weatherapi.utils.NOT_FOUND_CITY

class WeatherListViewModel(
    private val listWeather: MutableLiveData<List<WeatherModel>> =
        MutableLiveData<List<WeatherModel>>()
) : ViewModel() {

    fun cityListLiveData(): MutableLiveData<List<WeatherModel>> {
        return listWeather
    }

    private val weatherByNameCity: MutableLiveData<WeatherModel> = MutableLiveData<WeatherModel>()
    fun getWeatherByNameCity(): MutableLiveData<WeatherModel> {
        Log.d("myLogs", "WeatherListViewModel: weatherByNameCity")
        return weatherByNameCity
    }

    fun getCityList(network: Boolean) {
        Log.d("myLogs", "WeatherListViewModel: getCityList")
        if (network) {
            getCityListFromModel()
        } else {
            getCityListFromRoom()
        }
    }

    fun searchWeatherByNameCity(cityName: String) {
        RepositoryRetrofitImpl().getWeatherDetailsByCityName(cityName, object : ICallbackResponse {
            override fun onCallbackResponse(callbackWeather: WeatherModel) {
                weatherByNameCity.postValue(callbackWeather)
            }

            override fun onCallbackFailure(e: Exception) {
                weatherByNameCity.postValue(WeatherModel().apply {
                    this.cityName = NOT_FOUND_CITY
                })
            }
        })
    }

    // Завантаження міст з WeatherModel
    private fun getCityListFromModel(){
        listWeather.value = getWorldCities()
    }

    // Завантаження міст з Room
    private fun getCityListFromRoom() {
        RepositoryRoomImpl().getWeatherAll(){weathersFromRoom ->
            listWeather.postValue(weathersFromRoom)
        }
    }

    fun deleteAllFromRoom(){
        RepositoryRoomImpl().deleteAll()
    }

}