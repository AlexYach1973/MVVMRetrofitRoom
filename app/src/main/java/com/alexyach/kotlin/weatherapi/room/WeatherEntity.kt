package com.alexyach.kotlin.weatherapi.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val nameCity: String,
    val temperature: Double,
    val humidity: Int,
    val pressure: Int,
    val lat: Double,
    val lon: Double,
    var description: String,
    var date: String = "local",
    val icon: String = "@drawable/ic_network_locked"
)
