package com.alexyach.kotlin.weatherapi.repository.remote.retrofit.weatherDTO
import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("feels_like")
    var feelsLikeDto: Double,
    @SerializedName("humidity")
    var humidityDto: Int,
    @SerializedName("pressure")
    var pressureDto: Int,
    @SerializedName("temp")
    var tempDto: Double,
    @SerializedName("temp_max")
    var tempMaxDto: Double,
    @SerializedName("temp_min")
    var tempMinDto: Double
)