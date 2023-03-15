package com.example.hikingapp.View

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hikingapp.Adapter.RunAdapter
import com.example.hikingapp.Model.Run
import com.example.hikingapp.R
import com.example.hikingapp.databinding.FragmentActivityHistoryBinding
import com.example.hikingapp.databinding.FragmentDashboardBinding
import com.example.hikingapp.db.DBoperations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.osmdroid.util.GeoPoint


class FragmentActivityHistory : Fragment(),RunAdapter.OnItemClickListener {

    private var _binding: FragmentActivityHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbOperations: DBoperations
    private lateinit var adapter: RunAdapter
    private lateinit var listOfRunActivitiy : ArrayList<Run>
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentActivityHistoryBinding.inflate(inflater, container, false)
        binding.historyToolbar.apply {
            setNavigationIcon(R.drawable.baseline_arrow_back_24)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
        }

        dbOperations = DBoperations()
        listOfRunActivitiy = ArrayList()

        readData()
        adapter = RunAdapter(listOfRunActivitiy,this)
        // read data
        //dbOperations.readData(adapter)
        //listOfRunActivitiy = dbOperations.listOfRunActivity


        binding.apply {

            recyclerview.adapter = adapter
            recyclerview.setHasFixedSize(true)
            recyclerview.layoutManager = LinearLayoutManager(requireContext())

        }




        return binding.root
    }


    // bilgileri diğer fragmnet e aktardığımız yer
    override fun onItemClickButton(
        //id:String,
        date: String,
        distance: Double,
        time: String,
        route: ArrayList<GeoPoint>
    ) {


        val data: Array<String> = arrayOf(date,distance.toString(),time, route.toString())
        val action =  FragmentActivityHistoryDirections.actionFragmentActivityHistoryToFragmentActivityHistoryDetail(data)
        findNavController().navigate(action)
    }

    fun readData() {


        if (uid != null) {
            val database = FirebaseDatabase.getInstance().getReference("user").child(uid).child("Run")
            database.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        listOfRunActivitiy.clear()
                        for (runSnapshot in snapshot.children) {
                            //val id = runSnapshot.key.toString()
                            val date = runSnapshot.child("date").getValue(String::class.java) ?: ""
                            val distance = runSnapshot.child("distance").getValue(Double::class.java) ?: 0.0
                            val time = runSnapshot.child("time").getValue(String::class.java) ?: ""
                            val route = ArrayList<GeoPoint>()
                            runSnapshot.child("route").children.forEach { routeSnapshot ->
                                val lat = routeSnapshot.child("latitude").getValue(Double::class.java) ?: 0.0
                                val lon = routeSnapshot.child("longitude").getValue(Double::class.java) ?: 0.0
                                route.add(GeoPoint(lat, lon))
                            }
                            val run = Run(
                                //id,
                                date, distance, time, route)
                            listOfRunActivitiy.add(run)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //Toast.makeText(contex,"Veriler gelemedi",Toast.LENGTH_SHORT).show()
                }

            })
        }
    }


}