package com.example.hikingapp.View

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.hikingapp.BuildConfig
import com.example.hikingapp.Constants.API_KEY
import com.example.hikingapp.R
import com.example.hikingapp.Services.WeatherAPIService
import com.example.hikingapp.databinding.FragmentActivityBinding
import com.example.hikingapp.db.DBoperations
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


class FragmentActivity : Fragment() {
    private var _binding: FragmentActivityBinding? = null
    private val binding get() = _binding!!
    private  lateinit var  locationManager: LocationManager
    private var currentLocation: Location? = null
    private val userRoute = ArrayList<GeoPoint>()
    //private var userRouteOverlay = Polyline()
    val polyline = Polyline()
    private lateinit  var userMarker:Marker
    private lateinit var context1: Context
    private var totalDistance: Double = 0.0
    private var  previousLocation: Location? = null
    var isPlay = false
    var pauseOffSet :Long = 0
    private lateinit var dBoperations: DBoperations
    private var isLocationFounded:Boolean = false
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

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Location is being found...")

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID)
        //binding.progressBar.visibility = View.GONE

        binding.activityToolbar.apply {
            setNavigationIcon(R.drawable.baseline_arrow_back_24)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
        }


        //println("Activity Ekranındayım!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")


        dBoperations = DBoperations()

        mapSettings()
        polyline.color = Color.RED// Set the color of the path
        polyline.width = 5f // Set the width of the path
        binding.mapView.overlayManager.add(polyline)

        context1 = requireActivity().applicationContext
        locationManager = context1.getSystemService(LOCATION_SERVICE) as LocationManager


        lifecycleScope.launch {
            if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                currentLocation = getCurrentLocation()
                if (currentLocation != null){
                    val startPoint = GeoPoint(currentLocation!!.latitude,currentLocation!!.longitude)
                    binding.mapView.controller.setCenter(startPoint)
                    binding.mapView.controller.setZoom(16.0)
                    //userRoute.add(startPoint)
                    setWeather(currentLocation!!.latitude, currentLocation!!.longitude)
                    isLocationFounded = true

                }
                else{
                    println("Current location null")
                }

                userMarker = Marker(binding.mapView)

                // starter marker
                currentLocation?.let {
                    userMarker.position = GeoPoint(it.latitude, it.longitude)
                    userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    userMarker.icon = getDrawable(context1,R.drawable.baseline_location_on_24)
                    binding.mapView.overlays.add(userMarker)
                }

            }

        }



        //Update the users location in real time
        binding.mapView.overlays.add(Polyline().apply {
            polyline.setPoints(userRoute)
        })

        binding.apply {

            startEndButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    progressDialog.show()
                    //progressBar.visibility = View.VISIBLE
                    // Start tracking the user's location and updating the map
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,5f,locationListener)


                    // Add the user's route overlay to the map
                    binding.mapView.overlays.add(polyline)
                    //chronometer.start()

                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5f, locationListener)

                            while (!isLocationFounded) {
                                delay(1000)
                            }
                            progressDialog.dismiss()
                            startChronometer()
                        } catch (e: Exception) {
                            // Handle exception
                        }
                    }
                    /*
                    if (!isLocationFounded){
                        Toast.makeText(context1,"Searching for location. ",Toast.LENGTH_SHORT).show()

                    }

                     */


                }
                else {
                    locationManager.removeUpdates(locationListener)
                    stopChronometer()
                    //binding.progressBar.visibility = View.GONE
                    //Toast.makeText(context," Marker'ın durması lazım",Toast.LENGTH_SHORT).show()
                    //println(getCurrentTime())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val strDistance = String.format("%.2f", totalDistance / 1000)
                        totalDistance = strDistance.toDouble()
                        //dBoperations.saveData(getDate(),totalDistance,getCurrentTime(),userRoute)
                        print("total dıstance = "+totalDistance)
                    }

                }
            }

        }



        return binding.root
    }

    private val locationListener: LocationListener = object: LocationListener {
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onLocationChanged(location: Location) {
           // binding.progressBar.visibility = View.GONE

            isLocationFounded = true
            currentLocation = location
           // val currentPoint = GeoPoint(location.latitude,location.longitude)
            setWeather(location.latitude,location.longitude)

            userMarker.position = GeoPoint(location.latitude,location.longitude)
            userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            userMarker.icon = getDrawable(context1,R.drawable.baseline_location_on_24)
            binding.mapView.overlays.add(userMarker)

            calculateDistance(location)
            drawRouteLine(location)


        }
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            if (status == LocationProvider.AVAILABLE) {
                isLocationFounded = true
                if (binding.startEndButton.isChecked) {
                    startChronometer()
                }
            } else {
                isLocationFounded = false
            }
        }

    }


    private fun calculateDistance(location: Location){
        if (previousLocation == null){
            previousLocation = location
        }
        else{
            totalDistance += location.distanceTo(previousLocation!!)
            previousLocation = location
        }

        val strDistance = String.format("%.2f", totalDistance / 1000) + " km"
        //main thread
        activity?.runOnUiThread {
            binding.distanceTextView.text = strDistance
            //print(totalDistance)
        }

    }



    private fun drawRouteLine(location: Location) {
        val currentPoint = GeoPoint(location.latitude, location.longitude)

        // Add the point to the user's route
        userRoute.add(currentPoint)

        // Set the points of the polyline to the user's route
        polyline.setPoints(userRoute)

        // Move the map view to the current location
        binding.mapView.controller.animateTo(currentPoint)
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

    private fun setWeather(latitude:Double,longitude:Double){
        val weatherAPIService = WeatherAPIService()
        weatherAPIService.getCurrentWeather(latitude, longitude, API_KEY) { weatherResponse ->
            val degreeOfWeather = (weatherResponse.main.temp - 273.15).toInt()
            val main = weatherResponse.weather[0].main
            // Assuming you have an ImageView named "weatherIconImageView"
            println(weatherResponse.weather[0].icon)


            Picasso.get()
                .load("https://openweathermap.org/img/w/${weatherResponse.weather[0].icon}.png")
                .into(binding.weatherIcon)



            // val icon = weatherResponse.weather[3].icon
            //print(temperature)
            val degree = degreeOfWeather.toString() + " °C"
            binding.weatherTextview.text = main
            binding.degreeTextView.text =  degree
            // do something with the weather data here
        }


    }

    private suspend fun getCurrentLocation(): Location? = withContext(Dispatchers.IO) {
        if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } else {
            null
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

    fun startChronometer(){
        if (!isPlay){
            binding.chronometer.base = SystemClock.elapsedRealtime() - pauseOffSet
            binding.chronometer.start()
            isPlay  =true

        }
        else{
            binding.chronometer.base = SystemClock.elapsedRealtime()
            pauseOffSet = 0
            binding.chronometer.stop()
            isPlay = false
        }

    }
    fun stopChronometer(){
        if (isPlay){
            binding.chronometer.stop()
            pauseOffSet = SystemClock.elapsedRealtime() - binding.chronometer.base
            isPlay = false

        }
        else{
            binding.chronometer.base = SystemClock.elapsedRealtime() - pauseOffSet
            binding.chronometer.start()

            isPlay = true
        }
    }
    fun getCurrentElapsedTime(): Long {
        val base = binding.chronometer.base
        val elapsed = SystemClock.elapsedRealtime() - base
        return elapsed
    }
    fun getCurrentTime(): String {
        val elapsed = getCurrentElapsedTime()
        val hours = TimeUnit.MILLISECONDS.toHours(elapsed)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate():String{
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formattedDate = currentDate.format(formatter)
        return formattedDate

    }




}