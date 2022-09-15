package com.alexyach.kotlin.weatherapi.ui.weatherlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexyach.kotlin.weatherapi.di.SourcesModule
import com.alexyach.kotlin.weatherapi.model.WeatherModel
import com.alexyach.kotlin.weatherapi.model.getWorldCities
import com.alexyach.kotlin.weatherapi.repository.ICallbackResponse
import com.alexyach.kotlin.weatherapi.repository.IRepositoryByCityName
import com.alexyach.kotlin.weatherapi.repository.local.IWeatherRoom
import com.alexyach.kotlin.weatherapi.repository.local.RepositoryRoomImpl
import com.alexyach.kotlin.weatherapi.utils.NOT_FOUND_CITY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    @SourcesModule.RetrofitImpl
    val retrofitImpl : IRepositoryByCityName,
    private val roomImpl: IWeatherRoom
) : ViewModel() {

    private val listWeather: MutableLiveData<List<WeatherModel>> =
        MutableLiveData<List<WeatherModel>>()
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
        retrofitImpl.getWeatherDetailsByCityName(cityName, object : ICallbackResponse {
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
        roomImpl.getWeatherAll(){weathersFromRoom ->
            listWeather.postValue(weathersFromRoom)
        }
    }

    fun deleteAllFromRoom(){
        roomImpl.deleteAll()
    }

}