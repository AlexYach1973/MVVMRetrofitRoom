package com.alexyach.kotlin.weatherapi.ui.weatherlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexyach.kotlin.weatherapi.model.WeatherModel
import com.alexyach.kotlin.weatherapi.model.getWorldCities
import com.alexyach.kotlin.weatherapi.repository.ICallbackResponse
import com.alexyach.kotlin.weatherapi.repository.local.RepositoryRoomImpl
import com.alexyach.kotlin.weatherapi.repository.remote.retrofit.RepositoryRetrofitImpl

class WeatherListViewModel(
    private val listWeather: MutableLiveData<List<WeatherModel>> =
        MutableLiveData<List<WeatherModel>>()
) : ViewModel() {

    fun cityListLiveData(): MutableLiveData<List<WeatherModel>> {
        return listWeather
    }

    private val weatherByNameCity: MutableLiveData<WeatherModel> = MutableLiveData<WeatherModel>()
    fun getWeatherByNameCity(): MutableLiveData<WeatherModel> {
        return weatherByNameCity
    }

    fun getCityList(network: Boolean) {
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
                    this.cityName = "-1"
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