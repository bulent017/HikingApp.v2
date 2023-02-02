package com.example.hikingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.hikingapp.databinding.ActivityAuthBinding
import com.example.hikingapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backgroundImage: ImageView = binding.imageView
        val sideAnimation =AnimationUtils.loadAnimation(this,R.anim.slide)
        backgroundImage.startAnimation(sideAnimation)

        Handler().postDelayed({startActivity(Intent(this,AuthActivity::class.java))
                              finish()},4000)
    }


}