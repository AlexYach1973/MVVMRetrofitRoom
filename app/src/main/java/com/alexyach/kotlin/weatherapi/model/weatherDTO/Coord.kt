package com.alexyach.kotlin.weatherapi.model.weatherDTO
import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lat")
    var latDto: Double,
    @SerializedName("lon")
    var lonDto: Double
)