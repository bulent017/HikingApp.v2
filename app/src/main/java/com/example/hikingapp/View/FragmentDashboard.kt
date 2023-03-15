package com.example.hikingapp.View

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hikingapp.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.hikingapp.Model.Run
import com.example.hikingapp.Model.User
import com.example.hikingapp.R
import com.example.hikingapp.TrackingUtility
import com.example.hikingapp.databinding.FragmentDashboardBinding
import com.example.hikingapp.db.DBoperations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.TimeUnit


 class FragmentDashboard : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var runArrayList : ArrayList<Run>
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var progressDialog: ProgressDialog
    var totalDistance = 0.0
    var totalTime:String = ""
    var numberOfActivity = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        runArrayList = ArrayList()



        binding.apply {

            activityToolbar.setOnMenuItemClickListener{
                Firebase.auth.signOut()
                startActivity(Intent(requireContext(),AuthActivity::class.java))
                activity?.finish()
                true
            }
        }
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Loading...")
        progressDialog.setMessage("Please wait while we fetch the data")
        progressDialog.setCancelable(false)

// Show the progress dialog
        progressDialog.show()

        activity?.runOnUiThread {
            readDataFromDashboard()
        }

        binding.apply {


            startActivityButton.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentDashboard_to_fragmentActivity)

            }



        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
        //readDataFromDashboard()
    }

    private fun requestPermission() {
        if (TrackingUtility.hasLocationPermission(requireContext())) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                EasyPermissions.requestPermissions(
                    this,
                    "This application cannot be work without Location Permission",
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permissions to use this app",
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        } else {
            EasyPermissions.requestPermissions(
                this,
                "This application cannot be work without Location Permission",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // deprecated class yerine başka bir şey kullan daha sonra
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

      fun readDataFromDashboard() {

        if (uid != null) {
            val database = FirebaseDatabase.getInstance().getReference("user").child(uid).child("Run")
            database.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        runArrayList.clear()
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
                            runArrayList.add(run)
                        }
                    }
                    progressDialog.dismiss()
                    updateUI(runArrayList)
                }

                override fun onCancelled(error: DatabaseError) {
                    //Toast.makeText(contex,"Veriler gelemedi",Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }

            })
        }
    }

    fun getTotalTimeInMillis(listOfRun: ArrayList<Run>): Long {

        val timeList = ArrayList<String>()

        for (run:Run in listOfRun){
            timeList.add(run.time)
        }

        var totalTimeInMillis: Long = 0
        for (timeString in timeList) {
            val timeParts = timeString.split(":")
            val hours = timeParts[0].toLong()
            val minutes = timeParts[1].toLong()
            val seconds = timeParts[2].toLong()
            totalTimeInMillis += (hours * 3600 + minutes * 60 + seconds) * 1000
        }
        return totalTimeInMillis
    }

    fun getTotalDistance(listOfRun: ArrayList<Run>): Double{
        var distance = 0.0

        for ( run:Run in listOfRun){
            distance+=run.distance
        }
        return distance
    }
    private fun updateUI(runArrayList: ArrayList<Run>) {
        numberOfActivity = runArrayList.size
        totalDistance = getTotalDistance(runArrayList)
        totalTime = formatMillisToString(getTotalTimeInMillis(runArrayList))
            //getTotalTimeInMillis(runArrayList)

        val strDistance = String.format("%.1f Km",totalDistance)
        //val strTime = totalTime + "S"


        binding.totalDistanceTextView.text = strDistance
        binding.numberofActivityTextView.text = numberOfActivity.toString()
        binding.timeText.text = totalTime

    }
    fun formatMillisToString(totalTimeInMillis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(totalTimeInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(totalTimeInMillis - TimeUnit.HOURS.toMillis(hours))
        val seconds = TimeUnit.MILLISECONDS.toSeconds(totalTimeInMillis - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes))
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

 }
