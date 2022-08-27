package com.alexyach.kotlin.weatherapi.repository.remote.retrofit.weatherDTO
import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("country")
    var countryDto: String,
    @SerializedName("id")
    var idDto: Int,
    @SerializedName("message")
    var messageDto: Double,
    @SerializedName("sunrise")
    var sunriseDto: Int,
    @SerializedName("sunset")
    var sunsetDto: Int,
    @SerializedName("type")
    var typeDto: Int
)