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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.hikingapp.BuildConfig
import com.example.hikingapp.databinding.FragmentActivityBinding
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


        getMyLocation()



        binding.apply {
            //mapView.setScrollableAreaLimitDouble(BoundingBox(85.0, 100.0,-85.0,-100.0) )
            //mapView.maxZoomLevel = 20.0
           // mapView.minZoomLevel = 4.0
            //mapView.isHorizontalMapRepetitionEnabled= true
            //mapView.isVerticalMapRepetitionEnabled = false
            //mapView.setScrollableAreaLimitLatitude(MapView.getTileSystem().maxLatitude, MapView.getTileSystem().minLatitude,0)
            buttonStart.setOnClickListener {
                println("Start tıklandı")
            }



        }


        return binding.root
    }



    fun getMyLocation(){
        val provider = GpsMyLocationProvider(context)
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER)
        val myLocationNewOverlay = MyLocationNewOverlay(provider,binding.mapView)
        myLocationNewOverlay.enableMyLocation()
        binding.mapView.overlayManager.add(myLocationNewOverlay)

    }






}