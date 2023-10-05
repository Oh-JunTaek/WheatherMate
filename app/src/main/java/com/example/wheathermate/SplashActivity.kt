package com.example.wheathermate

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.wheathermate.databinding.ActivitySplashBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class SplashActivity : AppCompatActivity() {

    companion object{
        private const val RC_SIGN_IN= 1234 // 혹은 다른 숫자.
        private const val TAG= "SplashActivity"

    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivitySplashBinding
    private fun onContinueAsGuestClicked() {
        // 게스트로 계속하기 버튼이 클릭되었을 때 실행할 동작
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.signInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

            // Set login type to google
            sharedPreferences.edit().putString("loginType", "google").apply()
        }

        binding.guestLoginButton.setOnClickListener {
            onContinueAsGuestClicked()

            // Set login type to guest
            sharedPreferences.edit().putString("loginType", "guest").apply()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // 성공적으로 로그인한 경우 여기서 원하는 동작 수행 (예: HomeActivity로 넘어감)
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            finish()

        } catch (e: ApiException) {
            Log.e(TAG, "Google sign in failed", e)

            // 로그인 실패 시 예외 처리 (예 : 다시 Splash 화면에서 시작 등의 작업 수행 가능.)
        }
    }
}//1