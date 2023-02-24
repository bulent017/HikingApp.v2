package com.example.hikingapp.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import com.example.hikingapp.BuildConfig
import com.example.hikingapp.R
import com.example.hikingapp.databinding.FragmentActivityBinding
import kotlinx.coroutines.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.util.*
import kotlin.math.roundToLong


class FragmentActivity : Fragment() {
    private var _binding: FragmentActivityBinding? = null
    private val binding get() = _binding!!
    private  lateinit var  locationManager: LocationManager
    private var currentLocation: Location? = null
    private val userRoute = ArrayList<GeoPoint>()
    //private var userRouteOverlay = Polyline()
    private var startTime: Date? = null
    val polyline = Polyline()
    private lateinit  var userMarker:Marker
    private lateinit var context1: Context
    private var totalDistance: Double = 0.0
    private var  previousLocation: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("ServiceCast")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentActivityBinding.inflate(inflater,container,false)
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID)

        mapSettings()
        polyline.color = Color.RED// Set the color of the path
        polyline.width = 5f // Set the width of the path
        binding.mapView.overlayManager.add(polyline)

        context1 = requireActivity().applicationContext
        locationManager = context1.getSystemService(LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            //print("permission kısmında")
        }

        if (currentLocation != null){
            val startPoint = GeoPoint(currentLocation!!.latitude,currentLocation!!.longitude)
            binding.mapView.controller.setCenter(startPoint)
            binding.mapView.controller.setZoom(16.0)
            userRoute.add(startPoint)

        }

        userMarker = Marker(binding.mapView)

        // starter marker
        currentLocation?.let {
            userMarker.position = GeoPoint(it.latitude, it.longitude)
            userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            userMarker.icon = getDrawable(context1,R.drawable.baseline_location_on_24)
            binding.mapView.overlays.add(userMarker)
        }
        //Update the users location in real time
        binding.mapView.overlays.add(Polyline().apply {
            polyline.setPoints(userRoute)
        })

        binding.apply {

            startEndButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {

                    // Start tracking the user's location and updating the map
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
                    startTime = Calendar.getInstance().time

                    // Add the user's route overlay to the map
                    binding.mapView.overlays.add(polyline)
                    chronometer.start()

                }
                else {
                    locationManager.removeUpdates(locationListener)
                    Toast.makeText(context," Marker'ın durması lazım",Toast.LENGTH_SHORT).show()
                    chronometer.stop()
                }
            }

        }
        return binding.root
    }

    private val locationListener: LocationListener = object: LocationListener {
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onLocationChanged(location: Location) {
            currentLocation = location
            val currentPoint = GeoPoint(location.latitude,location.longitude)
            userMarker.position = GeoPoint(location.latitude,location.longitude)
            userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            userMarker.icon = getDrawable(context1,R.drawable.baseline_location_on_24)
            binding.mapView.overlays.add(userMarker)

            // Update the user's route
            userRoute.add(currentPoint)
            polyline.setPoints(userRoute)
            binding.mapView.controller.animateTo(currentPoint)


            // Calculate the distance between the new location and the previous location
            if (previousLocation != null) {
                val results = FloatArray(1)
                Location.distanceBetween(
                    previousLocation!!.latitude, previousLocation!!.longitude,
                    location.latitude, location.longitude, results
                )
                val distance = results[0]

                // Add the distance to the total distance
                totalDistance = totalDistance + distance.toDouble()
            }
            // Store the new location as the previous location
            previousLocation = location


            val strDistance = String.format("%.2f Km", totalDistance.roundToLong()/1000.0)

            activity?.runOnUiThread {
                binding.totalDistance.text = strDistance

            }


        }
    }



    private fun mapSettings(){
        binding.apply {
            val mapController = binding.mapView.controller

            mapController.setZoom(9.5)

            mapView.setMultiTouchControls(true)
            // bu kısımda infinite map kısmını çözdük
            // mapView.setScrollableAreaLimitDouble(BoundingBox(85.0, 180.0, -85.0, -180.0))
            mapView.maxZoomLevel = 20.0

            mapView.minZoomLevel = 4.0
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            //mapView.setBuiltInZoomControls(true)
            mapView.isHorizontalMapRepetitionEnabled = true
            mapView.isVerticalMapRepetitionEnabled = false
            mapView.setScrollableAreaLimitLatitude(
                MapView.getTileSystem().maxLatitude,
                MapView.getTileSystem().minLatitude,
                0
            )
        }
    }


    override fun onStart() {
        super.onStart()
        println("Onstart")

    }

    override fun onResume() {

        super.onResume()
        binding.mapView.onResume()

        println("Onresume")

    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()

        println("OnPause")

    }

    override fun onStop() {
        super.onStop()
        println("OnStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
        print("OnDestroy")
    }
}