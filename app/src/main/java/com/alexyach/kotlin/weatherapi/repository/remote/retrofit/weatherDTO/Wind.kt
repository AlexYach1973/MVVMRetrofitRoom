package com.alexyach.kotlin.weatherapi.repository.remote.retrofit.weatherDTO
import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("deg")
    var degDto: Int,
    @SerializedName("speed")
    var speedDto: Double
)