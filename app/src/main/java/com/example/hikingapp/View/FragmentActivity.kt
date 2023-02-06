package com.example.hikingapp.View

import android.app.Application
import android.content.Context

import android.os.Bundle
import android.os.StrictMode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hikingapp.BuildConfig
import com.example.hikingapp.databinding.FragmentActivityBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants


class FragmentActivity : Fragment() {
    private var _binding: FragmentActivityBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentActivityBinding.inflate(inflater,container,false)
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID)
        //OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);

        binding.apply {
            buttonStart.setOnClickListener {
                println("Start tıklandı")
            }



        }


        return binding.root
    }


}