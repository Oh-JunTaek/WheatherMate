package com.example.wheathermate

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/forecast")
    fun getForecast(@Query("lat") lat: String,
                    @Query("lon") lon: String,
                    @Query("appid") appid: String): Call<ForecastResponse>
}