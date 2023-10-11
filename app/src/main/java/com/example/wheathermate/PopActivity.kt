package com.example.wheathermate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
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
            0.90f // Adjust this value to change the amount of dim behind the dialog
        windowParams.width =
            (resources.displayMetrics.widthPixels * 0.8).toInt() // 80% width of screen
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT // Height as necessary
        window.attributes = windowParams


        // 인텐트에서 데이터 받아오기


        binding.btnedit.setOnClickListener {
            // Start TodoActivity
            val intent = Intent(this, TodoActivity::class.java)
            startActivity(intent)

            // Finish PopActivity
            finish()
        }

        binding.btnclose.setOnClickListener {
            finish()
        }

        // Get SharedPreferences
        // Get SharedPreferences
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)

        // previously selected date from the calendar
        val selectedDate = intent.getStringExtra("selected_date")

        if (selectedDate != null) {
            // Load data for the selected date
            val titleForSelectedDate = sharedPreferences.getString("$selectedDate-title", "")
            val contentForSelectedDate = sharedPreferences.getString("$selectedDate-content", "")

            if (titleForSelectedDate == "" && contentForSelectedDate == "") {
                binding.titleTextView.text = "일정 없음"
                binding.contentEditText.setText("일정 없음")
            } else {
                binding.titleTextView.text = titleForSelectedDate
                binding.contentEditText.setText(contentForSelectedDate)
            }//아래는 선택된 날짜의 날씨를 반영하기 위함
//            val weatherConditionForSelectedDate: String? = getWeatherCondition(selectedDate)
//
//            if(weatherConditionForSelectedDate != null) {
//                setWeatherIcon(weatherConditionForSelectedDate)
//            }
        } else {
            Toast.makeText(this@PopActivity,"No date is passed.",Toast.LENGTH_SHORT).show()
        }

        // Disable editing on the EditText.
        binding.contentEditText.isEnabled = false


    }

    private fun setWeatherIcon(weather: String) {
        val weatherCondition = weather

        when (weatherCondition) {
            "Rain" -> binding.imageView.setImageResource(R.drawable.rain)
            "Clear" -> binding.imageView.setImageResource(R.drawable.sun)
            "Snow" -> binding.imageView.setImageResource(R.drawable.snow)
            "Clouds" -> binding.imageView.setImageResource(R.drawable.clouds)
            else -> binding.imageView.setImageResource(R.drawable.default_icon)
        }
    }
}