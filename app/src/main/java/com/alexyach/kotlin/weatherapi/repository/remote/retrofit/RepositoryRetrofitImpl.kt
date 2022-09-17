package com.alexyach.kotlin.weatherapi.repository.remote.retrofit

import android.annotation.SuppressLint
import com.alexyach.kotlin.weatherapi.repository.ICallbackResponse
import com.alexyach.kotlin.weatherapi.repository.IRepositoryByCityName
import com.alexyach.kotlin.weatherapi.repository.IRepositoryByLocation
import com.alexyach.kotlin.weatherapi.repository.remote.retrofit.weatherDTO.WeatherDTO
import com.alexyach.kotlin.weatherapi.utils.WEATHER_API_KEY_VALUE
import com.alexyach.kotlin.weatherapi.utils.converterWeatherDtoToWeatherModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // або вказати в модулі SourcesModule()
class RepositoryRetrofitImpl @Inject constructor(
    retrofit: Retrofit
) : IRepositoryByCityName, IRepositoryByLocation {

    private val api = retrofit.create(IWeatherApiRetrofit::class.java)

    @SuppressLint("CheckResult")
    override fun getWeatherDetailsByCityName(
        cityName: String,
        callbackResponse: ICallbackResponse
    ) {

        val response: Observable<Response<WeatherDTO>> =
            api.getWeatherByCityName(WEATHER_API_KEY_VALUE, cityName)
        response.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { parseResponseCode(it, callbackResponse)},
                { callbackResponse.onCallbackFailure(IOException())}
            )
    }

    @SuppressLint("CheckResult")
    override fun getWeatherDetailsByLocation(
        lat: Double,
        lon: Double,
        callbackResponse: ICallbackResponse
    ) {

        val response: Observable<Response<WeatherDTO>> =
            api.getWeatherByLocation(WEATHER_API_KEY_VALUE, lat, lon)
        response.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {parseResponseCode(it, callbackResponse)},
                { callbackResponse.onCallbackFailure(IOException("Не відома помилка"))}
            )

    }

    private fun parseResponseCode(response: Response<WeatherDTO>, callback: ICallbackResponse) {

        if (response.body() != null) {
            when (response.code()) {
                in 200..299 -> callback.onCallbackResponse(
                    converterWeatherDtoToWeatherModel(response.body()!!)
                )

                429 -> callback.onCallbackFailure(IOException("Перевищений ліміт"))
                401 -> callback.onCallbackFailure(IOException("Щось не так з ключем API"))
                404 -> callback.onCallbackFailure(IOException("Не правильно введені дані"))
                500, 502, 503, 504 -> callback.onCallbackFailure(
                    IOException("Сервер іздох")
                )

                else -> callback.onCallbackFailure(IOException("Не відома помилка"))
            }

        } else {
            callback.onCallbackFailure(IOException(response.message()))
        }
    }

}
