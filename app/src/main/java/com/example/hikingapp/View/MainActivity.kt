package com.example.hikingapp.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hikingapp.databinding.ActivityAuthBinding
import com.example.hikingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}