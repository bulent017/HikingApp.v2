package com.example.hikingapp.Services

import com.example.hikingapp.Model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    //https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=6e3870f76d3238f42f8b0a1191e4b833


    @GET("data/2.5/weather")
    fun getData(
        @Query("lat")
        latitude:Double,
        @Query("lon")
        longitude:Double,
        @Query("appid") apiKey:String): Call<WeatherResponse>


}