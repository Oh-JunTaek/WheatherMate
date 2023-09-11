package com.example.wheathermate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.wheathermate.databinding.ActivityPopBinding

class PopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the width and height of the dialog
        val windowParams = window.attributes
        windowParams.dimAmount =
            0.70f // Adjust this value to change the amount of dim behind the dialog
        windowParams.width =
            (resources.displayMetrics.widthPixels * 0.8).toInt() // 80% width of screen
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT // Height as necessary
        window.attributes = windowParams


        // 인텐트에서 데이터 받아오기
        val selectedDate = intent.getStringExtra("selected_date")

        // 이제 selectedDate 변수에 선택된 날짜가 있으므로, 이를 사용하여 필요한 작업을 수행합니다.
    }
        private fun setWeatherIcon(weather: String) {
            val weatherCondition = weather

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
    }
