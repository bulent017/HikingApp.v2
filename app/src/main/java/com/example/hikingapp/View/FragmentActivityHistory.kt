package com.example.hikingapp.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hikingapp.Adapter.RunAdapter
import com.example.hikingapp.Model.Run
import com.example.hikingapp.R
import com.example.hikingapp.databinding.FragmentActivityHistoryBinding
import com.example.hikingapp.databinding.FragmentDashboardBinding
import com.example.hikingapp.db.DBoperations
import org.osmdroid.util.GeoPoint


class FragmentActivityHistory : Fragment(),RunAdapter.OnItemClickListener {

    private var _binding: FragmentActivityHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbOperations: DBoperations
    private lateinit var adapter: RunAdapter
    private lateinit var listOfRunActivitiy : ArrayList<Run>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentActivityHistoryBinding.inflate(inflater, container, false)


        dbOperations = DBoperations()



        listOfRunActivitiy = ArrayList()

        adapter = RunAdapter(listOfRunActivitiy,this)
        binding.apply {

            recyclerview.adapter = adapter
            recyclerview.setHasFixedSize(true)
            recyclerview.layoutManager = LinearLayoutManager(requireContext())

        }
        // read data
        dbOperations.readData(adapter)
        listOfRunActivitiy = dbOperations.listOfRunActivity



        return binding.root
    }


    // bilgileri diğer fragmnet e aktardığımız yer
    override fun onItemClickButton(
        id:String,
        date: String,
        distance: Double,
        time: String,
        route: ArrayList<GeoPoint>
    ) {

    }


}