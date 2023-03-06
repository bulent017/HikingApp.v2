package com.example.hikingapp.Model

import org.osmdroid.util.GeoPoint
import java.util.Date

/*
data class Run(
    var id: String,
    val date:String,
    val distance: Double,
    val time:String,
    val route:ArrayList<GeoPoint>){

}

 */



data class Run(
    var id: String = "",
    val date: String = "",
    val distance: Double = 0.0,
    val time: String = "",
    val route: ArrayList<GeoPoint> = ArrayList()
) {
    // Empty constructor required for Firebase deserialization
    constructor() : this("", "", 0.0, "", ArrayList())
}














