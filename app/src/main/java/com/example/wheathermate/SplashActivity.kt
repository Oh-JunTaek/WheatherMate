package com.example.wheathermate

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.wheathermate.databinding.ActivitySplashBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class SplashActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 1234 // 혹은 다른 숫자.
        private const val TAG = "SplashActivity"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000 // Arbitrary number for the request code.
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivitySplashBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Request location permissions from the user.
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "위치 권한이 승인되었습니다.", Toast.LENGTH_SHORT).show()

                // Check if the location permission has been granted before attempting to get the location.
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result != null) {
                            val location = task.result
                            val latitude = location.latitude
                            val longitude = location.longitude

                            sharedPreferences.edit().apply {
                                putFloat("latitude", latitude.toFloat())
                                putFloat("longitude", longitude.toFloat())
                                apply()
                            }
                        } else {
                            // 위치 정보를 가져오지 못했을 때 처리.
                            finish()
                        }

                        // Automatically continue as guest.
                        Handler(Looper.getMainLooper()).postDelayed({
                            onContinueAsGuestClicked()
                        }, 2000) // Delay of 2 seconds (2000 milliseconds)
                    }
                }

            } else {
                Toast.makeText(this, "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun onContinueAsGuestClicked() {
        // 게스트로 계속하기 버튼이 클릭되었을 때 실행할 동작
        val intent = Intent(this, HomeActivity::class.java)
        sharedPreferences.edit().putString("loginType", "guest").apply()

        startActivity(intent)
        finish()

    }
}