package com.example.wheathermate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 로딩이 끝난 후, HomeActivity를 실행하도록 합니다.
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            // HomeActivity를 시작한 후에는 SplashActivity를 종료합니다.
            finish()
        }, 4000)  // 4000ms(4초) 후에 run() 메서드를 실행합니다.
    }
}