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
            val title = binding.editTextText.text.toString()
            val content = binding.content.text.toString()

            // Get SharedPreferences
            val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            // previously selected date from the calendar
            val selectedDate = sharedPreferences.getString("selected_date", null)

            if (selectedDate != null) {
                // Save data for the selected date

                // Save data in SharedPreferences as you did before.
                editor.putString("$selectedDate-title", title)
                editor.putString("$selectedDate-content", content)

                // Apply changes and check if successful.
                if (editor.commit()) {  // commit() returns a boolean indicating success/failure.
                    Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()
                    navigateToHomeActivity()

                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this@TodoActivity, "No date is selected.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToHomeActivity() {
        // Start HomeActivity.
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

        // Finish TodoActivity.
        finish()
    }
}