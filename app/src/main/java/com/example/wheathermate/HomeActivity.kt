package com.example.wheathermate


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.wheathermate.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView
import javax.annotation.meta.When

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
            @Query("appid") apiKey: String,
            @Query("units") units: String
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
        val view = binding.root
        setContentView(view)


        // Retrofit 객체 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(WeatherApiService::class.java)

        // API 호출 함수
        fun fetchWeatherData(locationName: String, apiKey: String) {
            val call = service.getWeatherData(locationName, apiKey, "metric")

            call.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        val weatherData = response.body()
                        if (weatherData != null) {
                            val currentTemperatureCelsius = weatherData.main.temp.toInt()
                            val temperatureText =
                                "${currentTemperatureCelsius}°C"
                            binding.weatherTextView.text = temperatureText

                            val weatherCondition = weatherData.weather[0].main

                            // imageView에 대한 참조가 필요합니다.
                            // 예시로 binding.imageView로 가정했습니다.
                            when (weatherCondition) {
                                "Rain" -> binding.imageView.setImageResource(R.drawable.rain)
                                "Clear" -> binding.imageView.setImageResource(R.drawable.sun)
                                "Snow" -> binding.imageView.setImageResource(R.drawable.snow)
                                "Clouds" -> binding.imageView.setImageResource(R.drawable.clouds)
                                else -> binding.imageView.setImageResource(R.drawable.default_icon)
                            }
                        }
                    } else {
                        // 에러 메시지 출력
                        println("Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    println(t.message)
                }
            })
        }

        // 원하는 지역명과 실제 API 키로 대체해야 합니다.

        val apiKey = "BuildConfig.WEATHER_API_KEY"
        val cities = arrayOf("Seoul", "Busan", "Incheon", "Daegu" /* 다른 도시들 */)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.citySpinner.adapter = adapter

        binding.citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedCity = parent.getItemAtPosition(position).toString()
                fetchWeatherData(selectedCity, apiKey)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무것도 선택되지 않았을 때의 처리
            }
        }

        // 초기 날씨 정보 로딩 (예: 서울)
        fetchWeatherData("Seoul", apiKey)

        // 사용자가 선택한 날짜. 실제로는 달력에서 가져옵니다.
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"

            val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putString("selected_date", selectedDate)
            editor.apply()
        }

        // 버튼 클릭 리스너 설정
        binding.btnchk.setOnClickListener {
            val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)

            // previously selected date from the calendar
            val selectedDate = sharedPreferences.getString("selected_date", null)

            if (selectedDate != null) {
                // Load data for the selected date
                val titleForSelectedDate = sharedPreferences.getString("$selectedDate-title", "")
                val contentForSelectedDate =
                    sharedPreferences.getString("$selectedDate-content", "")

                if (titleForSelectedDate == "" && contentForSelectedDate == "") {
                    Toast.makeText(this@HomeActivity, "일정 없음", Toast.LENGTH_SHORT).show()
                } else {
                    // Use or display the loaded data...
                    // If you have a TextView to display this data, you can do something like:
                    // binding.myTextView.text = "$titleForSelectedDate\n$contentForSelectedDate"

                }

            } else {
                Toast.makeText(this@HomeActivity, "No date is selected.", Toast.LENGTH_SHORT).show()
            }

            // Intent 생성 및 데이터 추가
            val intent = Intent(this, PopActivity::class.java)
            intent.putExtra("selected_date", selectedDate)

            // 새로운 Activity 시작
            startActivity(intent)
        }
        binding.btnNavi.setOnClickListener {
            binding.layoutDrawer.openDrawer(GravityCompat.START)  // 'layoutDrawer'는 DrawerLayout의 ID입니다.
        }

        // NavigationView의 메뉴 아이템 선택에 대한 리스너 설정
        binding.NaviView.setNavigationItemSelectedListener(this)
        val headerView = layoutInflater.inflate(R.layout.nav_header, null)
        binding.NaviView.addHeaderView(headerView)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                Toast.makeText(applicationContext, "설정", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.notification -> {
                Toast.makeText(applicationContext, "알림", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.info -> {
                Toast.makeText(applicationContext, "정보", Toast.LENGTH_SHORT).show()
                true
            }

            else -> false
        }
    }
}