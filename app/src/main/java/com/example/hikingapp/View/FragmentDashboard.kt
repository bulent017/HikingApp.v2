package com.example.hikingapp.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.hikingapp.R
import com.example.hikingapp.databinding.FragmentDashboardBinding


class FragmentDashboard : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater,container,false)

        binding.apply {

           buttonNewActivity.setOnClickListener{
                findNavController().navigate(R.id.action_fragmentDashboard_to_fragmentActivity)

           }
            buttonActivityHistory.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentDashboard_to_fragmentActivityHistory)
            }


        }


        return binding.root
    }


}