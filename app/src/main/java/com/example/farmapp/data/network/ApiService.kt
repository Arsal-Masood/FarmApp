//package com.example.farmapp.data.network
//
//import com.example.farmapp.data.model.WeatherResponse
//import com.example.farmapp.data.model.SoilResponse
//import retrofit2.http.GET
//import retrofit2.http.Query
//
//interface ApiService {
//    // Weather API (WeatherAPI.com)
//    @GET("forecast.json")
//    suspend fun getWeather(
//        @Query("key") apiKey: String,
//        @Query("q") location: String,
//        @Query("days") days: Int = 2
//    ): WeatherResponse
//
//    @GET("latest")
//    suspend fun getSoilData(
//        @Query("lat") lat: Double,
//        @Query("lng") lng: Double,
//        @Query("apikey") apiKey: String
//    ): SoilResponse
//
//
//}

// WeatherApiService.kt
package com.example.farmapp.data.network

import com.example.farmapp.data.model.SoilResponse
import com.example.farmapp.data.model.WeatherResponse

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 2
    ): WeatherResponse
}

interface SoilApiService {
    @GET("latest")
    suspend fun getSoilData(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double
        ,@Header("x-api-key") apiKey: String
    ): SoilResponse
}
