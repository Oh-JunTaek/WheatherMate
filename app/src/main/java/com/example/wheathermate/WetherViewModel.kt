package com.example.wheathermate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel : ViewModel() {
    private val _weatherData = MutableLiveData<HomeActivity.WeatherResponse>()
    val weatherData: LiveData<HomeActivity.WeatherResponse> get() = _weatherData

    private val _forecastData = MutableLiveData<ForecastResponse>()
    val forecastData: LiveData<ForecastResponse> get() = _forecastData

    fun fetchWeatherData(locationName: String, apiKey: String) {
        // Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(HomeActivity.WeatherApiService::class.java)
        val call = service.getWeatherData(locationName, apiKey, "metric")

        call.enqueue(object : Callback<HomeActivity.WeatherResponse> {
            override fun onResponse(
                call: Call<HomeActivity.WeatherResponse>,
                response: Response<HomeActivity.WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    _weatherData.value = response.body()
                } else {
                    // 에러 메시지 출력...
                }
            }

            override fun onFailure(call: Call<HomeActivity.WeatherResponse>, t: Throwable) {
                println(t.message)

            }
        })
    }
    fun fetchForecastData(lat: String, lon: String, apiKey: String) {

        // Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // WeatherService 인터페이스의 구현체 생성
        val service = retrofit.create(WeatherService::class.java)

        // API 호출 준비
        val call = service.getForecast(lat, lon, apiKey)

        // API 호출 실행 및 응답 처리
        call.enqueue(object : Callback<ForecastResponse> {

            override fun onResponse(
                call: Call<ForecastResponse>,
                response: Response<ForecastResponse>
            ) {

                if (response.isSuccessful) {

                    // 응답이 성공적일 경우 LiveData 업데이트
                    _forecastData.value = response.body()

                } else {

                    // 에러 메시지 출력...
                }
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {

                println(t.message)

            }
        })
    }
}