package com.alexyach.kotlin.weatherapi.repository.remote.retrofit

import com.alexyach.kotlin.weatherapi.model.weatherDTO.WeatherDTO
import com.alexyach.kotlin.weatherapi.repository.ICallbackResponse
import com.alexyach.kotlin.weatherapi.repository.IRepositoryWeatherDetails
import com.alexyach.kotlin.weatherapi.utils.*
import com.alexyach.kotlin.weatherapi.utils.OPENWEATHERMAP_BASE_URL
import com.google.gson.GsonBuilder
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RepositoryRetrofitImpl : IRepositoryWeatherDetails {

    private val retrofitImpl = Retrofit.Builder().apply {
        baseUrl(OPENWEATHERMAP_BASE_URL)
        addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().setLenient().create())
        )
    }

    private val api: IWeatherApiRetrofit =
        retrofitImpl.build().create(IWeatherApiRetrofit::class.java)

    override fun getWeatherDetailsByCityName(
        cityName: String,
        callbackResponse: ICallbackResponse
    ) {

        api.getWeatherByCityName(WEATHER_API_KEY_VALUE, cityName)
            .enqueue(object : Callback<WeatherDTO> {

                override fun onResponse(
                    call: Call<WeatherDTO>, response: Response<WeatherDTO>
                ) {
                    if (response.body() != null) {

                        response.body()?.let {
                            when (response.code()) {
                                in 200..299 -> callbackResponse.onCallbackResponse(
                                    converterWeatherDtoToWeatherModel(it)
                                )

                                429 -> callbackResponse.onCallbackFailure(IOException("Перевищений ліміт"))
                                401 -> callbackResponse.onCallbackFailure(IOException("Щось не так з ключем API"))
                                404 -> callbackResponse.onCallbackFailure(IOException("Не правильно введені дані"))
                                500, 502, 503, 504 -> callbackResponse.onCallbackFailure(
                                    IOException("Сервер іздох")
                                )

                                else -> callbackResponse.onCallbackFailure(IOException("Не відома помилка"))
                            }
                        }

                    } else {
                        callbackResponse.onCallbackFailure(IOException(response.message()))
                    }
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    callbackResponse.onCallbackFailure(IOException("Не вдалося зв'язатися з сервером"))
                }

            })

    }
}