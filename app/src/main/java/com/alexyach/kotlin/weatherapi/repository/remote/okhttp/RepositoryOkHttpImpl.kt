package com.alexyach.kotlin.weatherapi.repository.remote.okhttp

import com.alexyach.kotlin.weatherapi.repository.remote.retrofit.weatherDTO.WeatherDTO
import com.alexyach.kotlin.weatherapi.repository.ICallbackResponse
import com.alexyach.kotlin.weatherapi.repository.IRepositoryByCityName
import com.alexyach.kotlin.weatherapi.utils.WEATHER_API_KEY_VALUE
import com.alexyach.kotlin.weatherapi.utils.toWeatherModel
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class RepositoryOkHttpImpl : IRepositoryByCityName {

    val client = OkHttpClient()
    val builder = Request.Builder()

    override fun getWeatherDetailsByCityName(
        cityName: String,
        callbackResponse: ICallbackResponse
    ) {

        builder.url("https://api.openweathermap.org/data/2.5/weather?q=${cityName}&appid=${WEATHER_API_KEY_VALUE}")
        val request: Request = builder.build()
        val call: Call = client.newCall(request)

        call.enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let {
                    when (response.code()) {
                        in 200..299 -> {
                            val responseString = it.string()
                            val weatherDTO = Gson().fromJson(responseString, WeatherDTO::class.java)
                            callbackResponse.onCallbackResponse(
                                weatherDTO.toWeatherModel()
                            )
                        }

                        429 -> callbackResponse.onCallbackFailure(IOException("Перевищений ліміт"))
                        401 -> callbackResponse.onCallbackFailure(IOException("Щось не так з ключем API"))
                        404 -> callbackResponse.onCallbackFailure(IOException("Не правильно введені дані"))
                        500, 502, 503, 504 -> callbackResponse.onCallbackFailure(IOException("Сервер здох"))

                        else -> callbackResponse.onCallbackFailure(IOException("Не відома помилка"))
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callbackResponse.onCallbackFailure(IOException("Не вдалося зв'язатися з сервером"))
            }
        })

    }
}