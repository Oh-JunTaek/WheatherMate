package com.example.wheathermate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.wheathermate.databinding.ActivityTodoBinding

class TodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btndone.setOnClickListener {
            saveTodo()
        }
    }

    private fun saveTodo() {
        val title = binding.editTextText.text.toString()
        val content = binding.content.text.toString()
        val selectedDate = getSelectedDate()

        if (selectedDate != null) {
            saveDataForSelectedDate(selectedDate, title, content)
        } else {
            Toast.makeText(this@TodoActivity, "No date is selected.", Toast.LENGTH_SHORT).show()
        }
    }//사용자의 입력 값을 저장

    private fun getSelectedDate(): String? {
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        return sharedPreferences.getString("selected_date", null)
    }//선택된 날짜 불러오기

    private fun saveDataForSelectedDate(date: String, title: String, content: String) {
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("$date-title", title)
        editor.putString("$date-content", content)

        if (editor.commit()) {
            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()
            navigateToHomeActivity()
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }//선택된 날짜에 대한 일 저장

    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }//homeactivity로 이동
}