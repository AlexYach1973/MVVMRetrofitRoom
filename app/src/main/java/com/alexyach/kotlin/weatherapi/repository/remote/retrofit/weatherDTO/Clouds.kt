package com.alexyach.kotlin.weatherapi.repository.remote.retrofit.weatherDTO
import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    var allDto: Int
)