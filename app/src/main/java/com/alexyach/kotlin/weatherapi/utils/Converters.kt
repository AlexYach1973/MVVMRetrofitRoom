package com.alexyach.kotlin.weatherapi.utils

import com.alexyach.kotlin.weatherapi.model.WeatherModel
import com.alexyach.kotlin.weatherapi.repository.remote.retrofit.weatherDTO.WeatherDTO
import com.alexyach.kotlin.weatherapi.room.WeatherEntity
import java.text.SimpleDateFormat
import java.util.*

fun WeatherDTO.toWeatherModel(): WeatherModel {
    val currentDate: String = SimpleDateFormat("dd MMM yyyy hh:mm:ss", Locale.ENGLISH)
        .format(Date())

    return WeatherModel(
        cityName = nameDto,
        lat = coordDto.latDto,
        lon = coordDto.lonDto,
        temperature = mainDto.tempDto,
        humidity = mainDto.humidityDto,
        pressure = mainDto.pressureDto,
        description = weatherDto[0].descriptionDto,
        currentDate, weatherDto[0].iconDto
    )
}

fun WeatherEntity.toWeatherModel() = WeatherModel(
    cityName = nameCity,
    lat = lat,
    lon = lon,
    temperature = temperature,
    humidity = humidity,
    pressure = pressure,
    description = description,
    date = date,
    icon = icon
)

fun WeatherModel.toWeatherEntity(): WeatherEntity {
    val currentDate: String = SimpleDateFormat("dd MMM yyyy hh:mm:ss", Locale.ENGLISH)
        .format(Date())

    return WeatherEntity(
        0,
        nameCity = cityName,
        temperature = temperature,
        humidity = humidity,
        pressure = pressure,
        lat = lat,
        lon = lon,
        description = description,
        currentDate,
        "@drawable.ic_network_locked"
    )
}


fun loadImageWeather(str: String): String {
    return when (str) {
        "01d" -> "https://openweathermap.org/img/wn/01d@2x.png"
        "02d" -> "https://openweathermap.org/img/wn/02d@2x.png"
        "03d" -> "https://openweathermap.org/img/wn/03d@2x.png"
        "04d" -> "https://openweathermap.org/img/wn/04d@2x.png"
        "09d" -> "https://openweathermap.org/img/wn/09d@2x.png"
        "10d" -> "https://openweathermap.org/img/wn/10d@2x.png"
        "11d" -> "https://openweathermap.org/img/wn/11d@2x.png"
        "13d" -> "https://openweathermap.org/img/wn/13d@2x.png"
        "50d" -> "https://openweathermap.org/img/wn/50d@2x.png"

        "01n" -> "https://openweathermap.org/img/wn/01nd@2x.png"
        "02n" -> "https://openweathermap.org/img/wn/02n@2x.png"
        "03n" -> "https://openweathermap.org/img/wn/03n@2x.png"
        "04n" -> "https://openweathermap.org/img/wn/04n@2x.png"
        "09n" -> "https://openweathermap.org/img/wn/09n@2x.png"
        "10n" -> "https://openweathermap.org/img/wn/10n@2x.png"
        "11n" -> "https://openweathermap.org/img/wn/11n@2x.png"
        "13n" -> "https://openweathermap.org/img/wn/13n@2x.png"
        "50n" -> "https://openweathermap.org/img/wn/50n@2x.png"

        else -> {
            "android.R.drawable.ic_lock_idle_lock"
        }
    }
}