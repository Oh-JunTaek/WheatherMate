package com.example.wheathermate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wheathermate.databinding.FragmentTodayBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale



class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getForecastData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getForecastData() {
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val apiKey = BuildConfig.WEATHER_API_KEY
        val latitude = sharedPreferences.getFloat("latitude", 0f).toDouble()
        val longitude = sharedPreferences.getFloat("longitude", 0f).toDouble()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)

        // 여기서 latitude와 longitude는 실제 위치 좌표값이어야 합니다.
        val call = service.getForecast(latitude.toString(), longitude.toString(), apiKey)

        call.enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(
                call: Call<ForecastResponse>, response:
                Response<ForecastResponse>
            ) {

                if (response.code() == 200) {
                    response.body()?.let { forecastResponse ->
                        val todayData = forecastResponse.list.filter { /* ... */ }
                        if (todayData.isNotEmpty()) {
                            val weatherCondition = todayData[0].weather[0].main

                            activity?.runOnUiThread {
                                binding.currentWeatherTextView.text = weatherCondition

                                when (weatherCondition) {
                                    "Rain" -> binding.weatherIcon.setImageResource(R.drawable.rain)
                                    "Clear" -> binding.weatherIcon.setImageResource(R.drawable.sun)
                                    "Snow" -> binding.weatherIcon.setImageResource(R.drawable.snow)
                                    "Clouds" -> binding.weatherIcon.setImageResource(R.drawable.clouds)
                                    else -> binding.weatherIcon.setImageResource(R.drawable.default_icon)
                                }
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                // Handle network errors here.
        }
    })
