package com.example.hikingapp.Model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.*
import org.osmdroid.util.GeoPoint
import java.lang.reflect.Type
import java.util.Date


data class Run(
    //var id: String,
    val date:String,
    val distance: Double,
    val time:String,
    val route:ArrayList<GeoPoint>){

}




/*
data class Run(
    //var id: String = "",
    val date: String = "",
    val distance: Double = 0.0,
    val time: String = "",
    val route: ArrayList<GeoPoint> = ArrayList()
) {
    // Empty constructor required for Firebase deserialization
    constructor() : this(
        //"",
        "", 0.0, "", ArrayList())
}

 */






/* // ilk çözüm
@IgnoreExtraProperties
data class Run(
    //var id: String? = null,
    val date: String? = null,
    val distance: Double? = null,
    val time: String? = null,
    val route: List<GeoPoint>? = null
) {
    @Suppress("unused")
    constructor() : this(
        //"",
        "",
        0.0,
        "",
        emptyList())
}

class GeoPointSerializer : JsonSerializer<GeoPoint> {
    override fun serialize(src: GeoPoint?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("latitude", src?.latitude ?: 0.0)
        jsonObject.addProperty("longitude", src?.longitude ?: 0.0)
        return jsonObject
    }
}

class GeoPointDeserializer : JsonDeserializer<GeoPoint> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): GeoPoint {
        val jsonObject = json?.asJsonObject
        val latitude = jsonObject?.get("latitude")?.asDouble ?: 0.0
        val longitude = jsonObject?.get("longitude")?.asDouble ?: 0.0
        return GeoPoint(latitude, longitude)
    }

}
 */



class RunDeserializer : JsonDeserializer<Run> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Run {
        val jsonObject = json?.asJsonObject
        val date = jsonObject?.get("date")?.asString ?: ""
        val distance = jsonObject?.get("distance")?.asDouble ?: 0.0
        val time = jsonObject?.get("time")?.asString ?: ""
        val routeJsonArray = jsonObject?.get("route")?.asJsonArray ?: JsonArray()

        val route = ArrayList<GeoPoint>()
        for (i in 0 until routeJsonArray.size()) {
            val routeJsonObject = routeJsonArray.get(i).asJsonObject
            val lat = routeJsonObject.get("latitude").asDouble
            val lng = routeJsonObject.get("longitude").asDouble
            route.add(GeoPoint(lat, lng))
        }

        return Run(date, distance, time, route)
    }
}












