package com.alexyach.kotlin.weatherapi.model.weatherDTO
import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    var allDto: Int
)