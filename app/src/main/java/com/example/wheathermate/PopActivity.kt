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

        // 다이얼로그의 너비와 높이 설정
        val windowParams = window.attributes
        windowParams.dimAmount = 0.90f // 다이얼로그 뒷 배경의 어두운 정도를 조정
        windowParams.width = (resources.displayMetrics.widthPixels * 0.8).toInt() // 화면 너비의 80%
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT // 높이는 필요한 만큼
        window.attributes = windowParams

        // "일정 편집" 버튼 클릭 리스너 설정
        binding.btnedit.setOnClickListener {
            val intent = Intent(this, TodoActivity::class.java)
            startActivity(intent)
            finish()
        }

        // "닫기" 버튼 클릭 리스너 설정
        binding.btnclose.setOnClickListener {
            finish()
        }

        // SharedPreferences 가져오기
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)

        // 캘린더에서 선택한 날짜 가져오기
        val selectedDate = intent.getStringExtra("selected_date")

        if (selectedDate != null) {
            // 선택한 날짜의 데이터 불러오기
            val titleForSelectedDate = sharedPreferences.getString("$selectedDate-title", "")
            val contentForSelectedDate = sharedPreferences.getString("$selectedDate-content", "")

            if (titleForSelectedDate == "" && contentForSelectedDate == "") {
                binding.titleTextView.text = "일정 없음"
                binding.contentEditText.setText("일정 없음")
            } else {
                binding.titleTextView.text = titleForSelectedDate
                binding.contentEditText.setText(contentForSelectedDate)
            }

            // 선택한 날짜의 날씨 상태에 따라 아이콘 설정
            // val weatherConditionForSelectedDate: String? = getWeatherCondition(selectedDate)
            // if(weatherConditionForSelectedDate != null) {
            //     setWeatherIcon(weatherConditionForSelectedDate)
            // }
        } else {
            Toast.makeText(this@PopActivity,"날짜가 선택되지 않았습니다.",Toast.LENGTH_SHORT).show()
        }

        // EditText 편집 비활성화
        binding.contentEditText.isEnabled = false
    }

    // 날씨 상태에 따른 아이콘 설정 함수
    private fun setWeatherIcon(weather: String) {
        when (weather) {
            "Rain" -> binding.imageView.setImageResource(R.drawable.rain)
            "Clear" -> binding.imageView.setImageResource(R.drawable.sun)
            "Snow" -> binding.imageView.setImageResource(R.drawable.snow)
            "Clouds" -> binding.imageView.setImageResource(R.drawable.clouds)
            else -> binding.imageView.setImageResource(R.drawable.default_icon)
        }
    }
}