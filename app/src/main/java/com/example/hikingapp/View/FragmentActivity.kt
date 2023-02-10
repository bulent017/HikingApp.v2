package com.example.hikingapp.View

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.hikingapp.BuildConfig
import com.example.hikingapp.databinding.FragmentActivityBinding
import kotlinx.coroutines.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.IconOverlay.ANCHOR_CENTER
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class FragmentActivity : Fragment() {
    private var _binding: FragmentActivityBinding? = null
    private lateinit var startPoint:GeoPoint
    private val binding get() = _binding!!
    private  var location: Location? = null
    private  var s: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentActivityBinding.inflate(inflater,container,false)
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID)

        binding.apply {
            val mapController = mapView.controller
            mapController.setZoom(9.5)

            mapView.setMultiTouchControls(true)
            // bu kısımda infinite map kısmını çözdük
            mapView.setScrollableAreaLimitDouble(BoundingBox(85.0, 180.0,-85.0,-180.0) )
            mapView.maxZoomLevel = 20.0

            mapView.minZoomLevel = 4.0
            mapView.isHorizontalMapRepetitionEnabled= true
            mapView.isVerticalMapRepetitionEnabled = false
            mapView.setScrollableAreaLimitLatitude(MapView.getTileSystem().maxLatitude, MapView.getTileSystem().minLatitude,0)
            buttonStart.setOnClickListener {
                println("Start tıklandı")
                location = getMyLocation3()
                println(location!!.altitude)
                println(location!!.latitude)
            }
            getMyLocation3()
            /*
            GlobalScope.launch(Dispatchers.Main) {

                val myLocation = getMyLocationAsync()
                //print(myLocation)
            }

             */
            //startPoint = GeoPoint()
            startPoint = GeoPoint(1.0,1.0)
            val startMaker = Marker(mapView)
            startMaker.position =startPoint
            startMaker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mapView.overlays.add(startMaker)
            mapView.controller.animateTo(startPoint)


        }


        return binding.root
    }

    /*
    fun getMyLocation2(): Location? {
        val provider = GpsMyLocationProvider(context)
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER)
        val myLocationNewOverlay = MyLocationNewOverlay(provider, binding.mapView)
        myLocationNewOverlay.enableMyLocation()
        binding.mapView.overlayManager.add(myLocationNewOverlay)
        return provider.lastKnownLocation
    }

     */
      fun  getMyLocation3(): Location?{
        val provider = GpsMyLocationProvider(context)
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER)
        val myLocationNewOverlay = MyLocationNewOverlay(provider,binding.mapView)
        myLocationNewOverlay.enableMyLocation()
        binding.mapView.overlayManager.add(myLocationNewOverlay)
        val myLocation = myLocationNewOverlay.lastFix

       // println("In my getMyLocation3() Function $myLocation")
        return myLocation
    }





    //startPoint = GeoPoint(myLocation.latitude, myLocation.longitude)


    // async func
    suspend fun getMyLocationAsync(): Location? {
        return withContext(Dispatchers.IO) {
            val provider = GpsMyLocationProvider(context)
            provider.addLocationSource(LocationManager.NETWORK_PROVIDER)
            val myLocationNewOverlay = MyLocationNewOverlay(provider, binding.mapView)
            myLocationNewOverlay.enableMyLocation()
            binding.mapView.overlayManager.add(myLocationNewOverlay)
            myLocationNewOverlay.lastFix

        }
    }





    override fun onStart() {
        super.onStart()
        println("Onstart")

    }

    override fun onResume() {

        super.onResume()
        println("Onresume")

    }

    override fun onPause() {
        super.onPause()
        println("OnPause")

    }

    override fun onStop() {
        super.onStop()
        println("OnStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        print("OnDestroy")
    }
}