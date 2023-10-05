package com.example.wheathermate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.wheathermate.databinding.ActivityTodoBinding
import com.google.firebase.firestore.FirebaseFirestore
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

                val loginType = sharedPreferences.getString("loginType", "")

                if (loginType == "guest") {
                    // Save data in SharedPreferences as you did before
                    editor.putString("$selectedDate-title", title)
                    editor.putString("$selectedDate-content", content)

                    // Apply changes and check if successful
                    if (editor.commit()) {  // commit() returns a boolean indicating success/failure
                        Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()
                        navigateToHomeActivity()

                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }

                } else if (loginType == "google") {
                    saveDataToFirebase(title, content)

                } else {
                    Toast.makeText(
                        this@TodoActivity,
                        "No valid login type found.",
                        Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this@TodoActivity, "No date is selected.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToHomeActivity() {
        // Start HomeActivity
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

        // Finish TodoActivity
        finish()
    }

    private fun saveDataToFirebase(title: String?, content: String?) {
        val db = FirebaseFirestore.getInstance()

        // Create a new user with a first and last name
        val user = hashMapOf(
            "title" to title,
            "content" to content
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "데이터가 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show()
                navigateToHomeActivity()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
    }
}//1