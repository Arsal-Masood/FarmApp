
package com.example.farmapp.data.repository

import com.example.farmapp.data.model.SoilResponse
import com.example.farmapp.data.model.WeatherResponse
import com.example.farmapp.data.network.RetrofitClient

class MainRepository {

    private val weatherApi = RetrofitClient.weatherApi   // Weather instance
    private val soilApi = RetrofitClient.soilApi         // Soil instance

    // Weather fetch
    suspend fun getWeather(apiKey: String, lat: Double, lon: Double): WeatherResponse {
        val location = "$lat,$lon"  // WeatherAPI expects this format
        return weatherApi.getWeather(apiKey, location)
    }

    // Soil fetch
    suspend fun getSoilData(lat: Double, lng: Double , apiKey: String): SoilResponse {
        return soilApi.getSoilData(lat, lng ,apiKey)
    }
}
