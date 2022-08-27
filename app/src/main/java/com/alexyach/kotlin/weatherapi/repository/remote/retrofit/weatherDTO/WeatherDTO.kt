package com.alexyach.kotlin.weatherapi.repository.remote.retrofit.weatherDTO
import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("base")
    var baseDto: String,
    @SerializedName("clouds")
    var cloudsDto: Clouds,
    @SerializedName("cod")
    var codDto: Int,
    @SerializedName("coord")
    var coordDto: Coord,
    @SerializedName("dt")
    var dtDto: Int,
    @SerializedName("id")
    var idDto: Int,
    @SerializedName("main")
    var mainDto: Main,
    @SerializedName("name")
    var nameDto: String,
    @SerializedName("sys")
    var sysDto: Sys,
    @SerializedName("timezone")
    var timezoneDto: Int,
    @SerializedName("visibility")
    var visibilityDto: Int,
    @SerializedName("weather")
    var weatherDto: List<Weather>,
    @SerializedName("wind")
    var windDto: Wind
)