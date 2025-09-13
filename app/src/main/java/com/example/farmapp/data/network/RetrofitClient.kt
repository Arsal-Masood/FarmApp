
package com.example.farmapp.data.network


import com.example.farmapp.data.model.SoilResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object RetrofitClient {

    // Weather API instance
    private const val WEATHER_BASE_URL = "https://api.weatherapi.com/v1/"
    val weatherApi: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    // Soil API instance (Ambee Data)
    private const val SOIL_BASE_URL = "https://api.ambeedata.com/soil/"
    val soilApi: SoilApiService by lazy {
        Retrofit.Builder()
            .baseUrl(SOIL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SoilApiService::class.java)
    }



}
