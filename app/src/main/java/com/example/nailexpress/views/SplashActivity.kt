package com.example.nailexpress.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var keepSplash = true
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition{
            keepSplash
        }
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(2000)
            keepSplash = false
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}