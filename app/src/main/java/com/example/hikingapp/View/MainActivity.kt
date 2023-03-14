package com.example.hikingapp.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hikingapp.R
import com.example.hikingapp.databinding.ActivityAuthBinding
import com.example.hikingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment  = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.activityMainBottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{_,destination,_->

            when(destination.id){
                R.id.fragmentActivityHistory -> showBottomNav()
                R.id.fragmentDashboard -> showBottomNav()
                R.id.fragmentActivity -> hideBottomNav()
                R.id.fragmentActivityHistoryDetail -> hideBottomNav()

            }

        }


    }



    private fun showBottomNav() {
        binding.activityMainBottomNavigationView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        binding.activityMainBottomNavigationView.visibility  = View.GONE

    }
}