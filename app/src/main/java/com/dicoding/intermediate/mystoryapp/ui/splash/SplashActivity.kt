package com.dicoding.intermediate.mystoryapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.lifecycleScope
import com.dicoding.intermediate.mystoryapp.R
import com.dicoding.intermediate.mystoryapp.data.UserPreference
import com.dicoding.intermediate.mystoryapp.data.dataStore
import com.dicoding.intermediate.mystoryapp.ui.main.MainActivity
import com.dicoding.intermediate.mystoryapp.ui.welcome.WelcomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val userPreference = UserPreference.getInstance(dataStore)

        Handler(Looper.myLooper()!!).postDelayed({
            lifecycleScope.launch {
                val userModel = userPreference.getSession().first()
                val intent = if (userModel.isLogin) {
                    Intent(this@SplashActivity, MainActivity::class.java)
                } else {
                    Intent(this@SplashActivity, WelcomeActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
        }, 3000)
    }
}
