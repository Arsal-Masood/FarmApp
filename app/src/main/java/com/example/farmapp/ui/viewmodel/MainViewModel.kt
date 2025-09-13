
package com.example.farmapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.farmapp.data.model.Resource
import com.example.farmapp.data.model.SoilResponse
import com.example.farmapp.data.model.WeatherResponse
import com.example.farmapp.data.network.SoilApiService
import com.example.farmapp.data.repository.MainRepository
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _weather = MutableLiveData<Resource<WeatherResponse>>()
    val weather: LiveData<Resource<WeatherResponse>> get() = _weather

    private val _soil = MutableLiveData<Resource<SoilResponse>>()
    val soil: LiveData<Resource<SoilResponse>> get() = _soil

    // Weather fetch
    fun fetchWeather(apiKey: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            _weather.postValue(Resource.Loading())
            try {
                val response = repository.getWeather(apiKey, lat, lon)
                _weather.postValue(Resource.Success(response))
            } catch (e: Exception) {
                Log.e("MainViewModel", "Weather fetch error: ${e.message}")
                _weather.postValue(Resource.Error(e.message ?: "Error fetching weather"))
            }
        }
    }

   //  Soil fetch (Ambee Soil requires apiKey)
    fun fetchSoil(lat: Double, lon: Double, apiKey: String) {
       viewModelScope.launch {
           _soil.postValue(Resource.Loading())
           try {
               val response = repository.getSoilData(lat, lon, apiKey)
               _soil.postValue(Resource.Success(response))
           } catch (e: Exception) {
               Log.e("MainViewModel", "Soil fetch error: ${e.message}")
               _soil.postValue(Resource.Error(e.message ?: "Error fetching soil data"))
           }
       }


   }}