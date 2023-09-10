package com.example.wheathermate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.wheathermate.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    data class WeatherResponse(
        val weather: List<Weather>,
        val main: Main,
        // 필요한 날씨 정보들을 포함하는 다른 필드들도 추가할 수 있습니다.
    )

    data class Weather(
        val id: Int,
        val main: String,
    )

    data class Main(
        val temp: Double,
    )

    interface WeatherApiService {
        @GET("weather")
        fun getWeatherData(
            @Query("q") location: String,
            @Query("appid") apiKey: String
            // 기타 필요한 쿼리 파라미터들을 추가할 수 있습니다.
            // 예시) @Query("units") units: String (온도 단위 설정)
            // 예시) @Query("lang") langCode : String (언어 설정)
        ): Call<WeatherResponse>
    }

    private lateinit var weatherTextView: TextView
    private lateinit var binding: ActivityHomeBinding
    private lateinit var service: WeatherApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(WeatherApiService::class.java)

        // API 호출 함수
        fun fetchWeatherData(locationName: String, apiKey: String) {
            val call = service.getWeatherData(locationName, apiKey)

            call.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        val weatherData = response.body()
                        if (weatherData != null) {
                            val currentTemperature = weatherData.main.temp.toInt()
                            val temperatureText =
                                "Current Temperature : $currentTemperature°C"
                            binding.weatherTextView.text = temperatureText
                        }
                    } else {
                        // 에러 메시지 출력
                        println("Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call : Call<WeatherResponse>, t : Throwable){
                    println(t.message)
                }
            })
        }

        // 원하는 지역명과 실제 API 키로 대체해야 합니다.
        val locationName = "Seoul"
        val apiKey = "1cf6f6e62d8764d472500d3b00e1b455"

        fetchWeatherData(locationName, apiKey)
    }
}