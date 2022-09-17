package com.alexyach.kotlin.weatherapi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherModel(
    var cityName: String = "",
    val lat: Double = 50.4333,
    val lon: Double = 30.5167,
    val temperature: Double = 20.0,
    val humidity: Int = 100,
    val pressure: Int = 999,
    val description: String = "",
    val date: String = "зараз",
    val icon: String = "android.R.drawable.ic_lock_idle_lock"
) : Parcelable

fun getWorldCities(): List<WeatherModel> {
    return listOf(
        WeatherModel("Київ", 50.4333, 30.5167),
        WeatherModel("Львів", 49.83968, 24.02972),
        WeatherModel("Харків", 49.9808, 36.2527),
        WeatherModel("Херсон", 46.63542, 32.61687),
        WeatherModel("Одеса", 46.48253, 30.72331),
        WeatherModel("Тернопіль", 49.5534, 25.5892),
        WeatherModel("Гостомель", 50.56841, 30.2651),
        WeatherModel("Мелітополь", 46.84891, 35.36533),
        WeatherModel("Буча", 50.54345, 30.21201),
        WeatherModel("Ірпінь", 50.521750000, 30.250550000),
        WeatherModel("London", 51.5085300, -0.1257400),
        WeatherModel("Tokyo", 35.6895000, 139.6917100),
        WeatherModel("Paris", 48.8534100, 2.3488000),
        WeatherModel("Berlin", 52.5244, 13.4105),
        WeatherModel("Roma", 43.2128, -75.4557),
        WeatherModel("Minsk", 53.90453979999999, 27.561524400000053),
        WeatherModel("Stamboul", 36.7708, 3.2265),
        WeatherModel("Washington", 38.9071923, -77.03687070000001),
        WeatherModel("Beijing", 39.90419989999999, 116.40739630000007)
    )
}