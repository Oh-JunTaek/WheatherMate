package com.example.wheathermate

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val list : List<WeatherData>
)

data class WeatherData(
    val dt_txt : String,
    val main : HomeActivity.Main,
    val weather : List<HomeActivity.Weather>,
    val rain : Rain?
)

data class Rain(
    @SerializedName("3h")
    var h3 : Float?
)