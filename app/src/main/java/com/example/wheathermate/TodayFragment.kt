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
        val coordinates = getCoordinates()
        val service = createWeatherService()
        getForecastData(service, coordinates)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCoordinates(): Pair<Double, Double> {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val latitude = sharedPreferences.getFloat("latitude", 0f).toDouble()
        val longitude = sharedPreferences.getFloat("longitude", 0f).toDouble()
        return Pair(latitude, longitude)
    }//사용자의 위치 좌표 가져오기

    private fun createWeatherService(): WeatherService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(WeatherService::class.java)
    }//날씨api호출

    private fun getForecastData(service: WeatherService, coordinates: Pair<Double, Double>) {
        val apiKey = BuildConfig.WEATHER_API_KEY
        val call = service.getForecast(coordinates.first.toString(),
            coordinates.second.toString(), apiKey)

        call.enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(
                call: Call<ForecastResponse>, response:
                Response<ForecastResponse>
            ) {
                if (response.code() == 200) {
                    response.body()?.let { forecastResponse ->
                        updateUIWithWeatherData(forecastResponse)
                    }
                }
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                // Handle network errors here.
            }
        })
    }//날씨 예보를 가져옴

    private fun updateUIWithWeatherData(forecastResponse: ForecastResponse) {
        val todayData = forecastResponse.list.toList()
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
    }//날씨 정보를 기반으로 UI업데이트
}