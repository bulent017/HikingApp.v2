package com.example.hikingapp.Services

import com.example.hikingapp.Constants.BASE_URL
import com.example.hikingapp.Model.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIService {
    private val BASE_URL = "https://api.openweathermap.org/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val weatherAPI = retrofit.create(WeatherAPI::class.java)

    fun getCurrentWeather(latitude: Double, longitude: Double, apiKey: String, callback: (WeatherResponse) -> Unit) {
        weatherAPI.getData(latitude, longitude, apiKey).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                val weatherResponse = response.body()
                if (weatherResponse != null) {
                    callback(weatherResponse)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}



