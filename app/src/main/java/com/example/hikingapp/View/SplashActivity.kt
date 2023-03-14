package com.example.hikingapp.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.hikingapp.R
import com.example.hikingapp.databinding.ActivitySplashBinding

//@SuppressLint("CustomSplashScreen") ekle sonra
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backgroundImage: ImageView = binding.imageView
        val header: TextView = binding.textView
        val sideAnimation =AnimationUtils.loadAnimation(this, R.anim.slide)
        val topAnimation = AnimationUtils.loadAnimation(this,R.anim.top)
        backgroundImage.startAnimation(sideAnimation)
        header.startAnimation(topAnimation)

        Handler(Looper.getMainLooper()).postDelayed({startActivity(Intent(this, AuthActivity::class.java))
                              finish()},4000)
    }


}