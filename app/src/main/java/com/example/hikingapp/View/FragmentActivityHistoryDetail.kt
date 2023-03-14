package com.example.hikingapp.View

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.hikingapp.BuildConfig
import com.example.hikingapp.R

import com.example.hikingapp.databinding.FragmentActivityHistoryDetailBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline



class FragmentActivityHistoryDetail : Fragment() {

    private var _binding: FragmentActivityHistoryDetailBinding? = null
    private lateinit  var userMarker:Marker
    private lateinit var context1: Context
    private val binding get() = _binding!!
     val polyline = Polyline()
    private val userRoute = ArrayList<GeoPoint>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentActivityHistoryDetailBinding.inflate(inflater, container, false)

        binding.detailsToolbar.apply {
            setNavigationIcon(R.drawable.baseline_arrow_back_24)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
        }
        mapSettings()



        context1 = requireActivity().applicationContext

        polyline.color = Color.RED// Set the color of the path
        polyline.width = 5f // Set the width of the path
        binding.mapView.overlayManager.add(polyline)
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID)
        arguments?.let {
            val data = FragmentActivityHistoryDetailArgs.fromBundle(it).runDataArray
            data.let { dataArray ->
                binding.apply {
                    println("distance: "+dataArray[1])
                    println("time: "+dataArray[2])

                    println(dataArray[3])
                    val endPointList= convertGeopointArraylist(dataArray[3])
                    drawMap()
                    setZoomMap(endPointList)
                    setMarker(endPointList)
                    // set distance and time
                    distanceTextView.text = dataArray[1]
                    timeTextview.text = dataArray[2]
                }
            }
        }



        return binding.root

    }

    private fun convertGeopointArraylist(dataString: String): ArrayList<Double>{
        val strArray = dataString.trim('[', ']').split(",")
        val doubleArray = strArray.map { it.toDouble() }.toDoubleArray()
        var lat  = 0.0
        var long = 0.0
        val filteredArray = doubleArray.filter { it != 0.0 }.toTypedArray()// remove altitude values
        // lat-long
        for (i in 0 until filteredArray.size step 2) {
            lat = filteredArray[i]
            long = filteredArray[i + 1]
            val point = GeoPoint(lat,long)
            userRoute.add(point)
        }
        val listEndPoint:ArrayList<Double> = ArrayList()
        listEndPoint.add(lat)
        listEndPoint.add(long)
        return  listEndPoint

    }


    private fun drawMap() {

        binding.mapView.overlays.add(Polyline().apply {
            polyline.setPoints(userRoute)
        })

    }
    private fun setZoomMap(latLongSet:ArrayList<Double>){

        val endPoint = GeoPoint(latLongSet.get(0),latLongSet.get(1))
        binding.mapView.controller.setCenter(endPoint)
        binding.mapView.controller.setZoom(17.0)

    }

    private fun setMarker(latLongSet:ArrayList<Double>){
        val endPoint = GeoPoint(latLongSet.get(0),latLongSet.get(1))
        userMarker = Marker(binding.mapView)
        userMarker.position = GeoPoint(endPoint.latitude, endPoint.longitude)
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        userMarker.icon = ContextCompat.getDrawable(context1, R.drawable.baseline_location_on_24)
        binding.mapView.overlays.add(userMarker)

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


}