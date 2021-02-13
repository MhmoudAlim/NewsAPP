package com.mahmoudalim.mvvmnewsapp.ui

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.support.v4.media.RatingCompat
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import com.mahmoudalim.mvvmnewsapp.R
import com.mahmoudalim.mvvmnewsapp.databinding.ActivityMainBinding
import com.mahmoudalim.mvvmnewsapp.models.NewsResponse
import com.mahmoudalim.mvvmnewsapp.util.Constants.Companion.SPLASH
import com.mahmoudalim.mvvmnewsapp.util.MyApplication
import com.mahmoudalim.mvvmnewsapp.util.Resource
import kotlinx.coroutines.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val lott: LottieAnimationView? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.splashScreenTheme)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.splashAnim

        GlobalScope.launch(Dispatchers.IO) {
            delay(SPLASH)
            val intent = Intent(this@MainActivity, NewsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}



