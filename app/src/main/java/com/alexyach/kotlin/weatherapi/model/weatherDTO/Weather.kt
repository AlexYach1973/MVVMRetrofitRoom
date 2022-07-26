package com.alexyach.kotlin.weatherapi.model.weatherDTO
import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("description")
    var descriptionDto: String,
    @SerializedName("icon")
    var iconDto: String,
    @SerializedName("id")
    var idDto: Int,
    @SerializedName("main")
    var mainDto: String
)