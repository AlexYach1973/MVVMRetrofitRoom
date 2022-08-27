package com.alexyach.kotlin.weatherapi.repository.remote.retrofit.weatherDTO
import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lat")
    var latDto: Double,
    @SerializedName("lon")
    var lonDto: Double
)