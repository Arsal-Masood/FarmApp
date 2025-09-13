//package com.example.farmapp.ui.view
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.location.Location
//import android.os.Bundle
//import android.widget.TextView
//import android.widget.Toast
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import com.example.farmapp.R
//import com.example.farmapp.data.model.Resource
//import com.example.farmapp.data.repository.MainRepository
//import com.example.farmapp.ui.viewmodel.MainViewModel
//import com.example.farmapp.ui.viewmodel.MainViewModelFactory
//import com.example.farmapp.utils.LanguageDialog
//import com.example.farmapp.utils.LanguageHelper
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var tvLocation: TextView
//    private lateinit var tvWeatherToday: TextView
//    private lateinit var tvWeatherTomorrow: TextView
//    private lateinit var tvSoilData: TextView
//
//    private val viewModel: MainViewModel by viewModels {
//        MainViewModelFactory(MainRepository())
//    }
//
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private val WEATHER_API_KEY = "3c5393ac21304a2383d172454250809" // 👈 yaha dalna hoga
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        tvLocation = findViewById(R.id.tvLocation)
//        tvWeatherToday = findViewById(R.id.tvWeatherToday)
//        tvWeatherTomorrow = findViewById(R.id.tvWeatherTomorrow)
//        tvSoilData = findViewById(R.id.tvSoilData)
//        viewModel.weather.observe(this) { resource ->
//            when(resource) {
//                is Resource.Loading -> { /* show loader */ }
//                is Resource.Success -> {
//                    val weather = resource.data
//                    tvLocation.text = weather?.location?.name
//                    tvWeatherToday.text = "Today: ${weather?.forecast?.forecastday?.get(0)?.day?.condition?.text}"
//                    tvWeatherTomorrow.text = "Tomorrow: ${weather?.forecast?.forecastday?.get(1)?.day?.condition?.text}"
//                }
//                is Resource.Error -> {
//                    tvWeatherToday.text = "Error: ${resource.message}"
//                }
//            }
//        }
////
////        viewModel.soil.observe(this) { resource ->
////            when (resource) {
////                is Resource.Loading -> {
////                    tvSoilData.text = "Loading..."
////                    Toast.makeText(this, "Fetching soil data...", Toast.LENGTH_SHORT).show()
////                }
////                is Resource.Success -> {
////                    val features = resource.data?.features
////                    if (!features.isNullOrEmpty()) {
////                        tvSoilData.text = features.joinToString("\n") { feature ->
////                            val p = feature.properties
////                            "Type: ${p.soil_type}, Depth: ${p.depth_cm}cm, Moisture: ${p.moisture_content}%, pH: ${p.ph}, OC: ${p.organic_carbon}%"
////                        }
////                        Toast.makeText(this, "Soil data fetched successfully!", Toast.LENGTH_SHORT).show()
////                    } else {
////                        tvSoilData.text = "No soil data"
////                        Toast.makeText(this, "No soil data returned for these coordinates.", Toast.LENGTH_LONG).show()
////                    }
////                }
////                is Resource.Error -> {
////                    tvSoilData.text = "Error: ${resource.message}"
////                    Toast.makeText(this, "Error fetching soil data: ${resource.message}", Toast.LENGTH_LONG).show()
////                }
////            }
////        }
//
//        viewModel.soil.observe(this) { resource ->
//            when (resource) {
//                is Resource.Loading -> tvSoilData.text = "Loading..."
//                is Resource.Success -> {
//                    val soc = resource.data?.properties?.layers?.soc
//                    if (soc != null) {
//                        tvSoilData.text = soc.depths.entries.joinToString("\n") { (depth, value) ->
//                            "$depth: ${value.mean} ${soc.unit}"
//                        }
//                    } else {
//                        tvSoilData.text = "No soil data"
//                    }
//                }
//                is Resource.Error -> tvSoilData.text = "Error: ${resource.message}"
//            }
//        }
//
//
//        // Check if language already selected
//        val prefs = getSharedPreferences("farmapp_prefs", Context.MODE_PRIVATE)
//        val langSelected = prefs.getString("selected_lang", null)
//
//        if (langSelected == null) {
//            // Agar language select nahi hui → show dialog
//            LanguageDialog(this).showLanguageDialog { lang ->
//                // Save selected language
//                prefs.edit().putString("selected_lang", lang).apply()
//
//                // Apply locale
//                LanguageHelper.setLocale(this, lang)
//
//                // Activity ko ek hi baar recreate karo
//                recreate()
//            }
//        }
//
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        fetchLocationAndData()
//    }
//
//
//    private fun fetchLocationAndData() {
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    100
//                )
//                return
//            }
//
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                location?.let {
//                    val lat = it.latitude
//                    val lon = it.longitude
//
//                    viewModel.fetchWeather(WEATHER_API_KEY, lat, lon)
//                    viewModel.fetchSoil(lat, lon)
//                }
//            }
//        }
//    }
//

package com.example.farmapp.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.health.connect.datatypes.units.Temperature
import android.location.Location
import android.media.VolumeShaper.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.farmapp.R
import com.example.farmapp.data.model.Resource
import com.example.farmapp.data.network.SoilApiService
import com.example.farmapp.data.repository.MainRepository
import com.example.farmapp.ui.viewmodel.MainViewModel
import com.example.farmapp.ui.viewmodel.MainViewModelFactory
import com.example.farmapp.utils.LanguageDialog

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvLocation: TextView
    private lateinit var tvWeatherToday: TextView
    private lateinit var tvWeatherTomorrow: TextView
    private lateinit var tvSoilData: TextView
    private lateinit var tvTemp :TextView


    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(MainRepository())
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val WEATHER_API_KEY = "3c5393ac21304a2383d172454250809"
    private val SOIL_API_KEY = "1f2c8e36eead0da99bbc306f6755c00ab9a100957ee6b89ef3fa1ac3d2f4288f"

    companion object{
        var selectedLanguage :String ="en"
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        lifecycleScope.launch {
//            try {
//                val response = viewModel.api(28.67, 77.23, "1f2c8e36eead0da99bbc306f6755c00ab9a100957ee6b89ef3fa1ac3d2f4288f")
//                Log.d("SoilTest", "Success: arsal $response")
//            } catch (e: Exception) {
//                Log.e("SoilTest", "Error: ${e.message}")
//            }
//        }


        tvLocation = findViewById(R.id.tvLocation)
        tvWeatherToday = findViewById(R.id.tvWeatherToday)
        tvWeatherTomorrow = findViewById(R.id.tvWeatherTomorrow)
        tvSoilData = findViewById(R.id.tvSoilData)
        tvTemp=findViewById(R.id.tvTemp)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Language selection check
//        val prefs = getSharedPreferences("farmapp_prefs", Context.MODE_PRIVATE)
//        val langSelected = prefs.getString("selected_lang", null)
//        if (langSelected == null) {
//            LanguageDialog(this).showLanguageDialog { lang ->
//                prefs.edit().putString("selected_lang", lang).apply()
//                LanguageHelper.setLocale(this, lang)
//                recreate()
//            }
//        }
//        val prefs = getSharedPreferences("farmapp_prefs", Context.MODE_PRIVATE)
//        val langSelected = prefs.getString("selected_lang", null)
//
//        if (langSelected == null) {
//            // Sirf first time dialog dikhana
//            LanguageDialog(this).showLanguageDialog { lang ->
//                prefs.edit().putString("selected_lang", lang).apply()
//                selectedLanguage = lang
//                LanguageHelper.setLocale(this, lang)
//                recreate()
//            }
//        } else {
//            // Agar pehle se select hai, usi ko apply kar do
//            selectedLanguage = langSelected
//            LanguageHelper.setLocale(this, langSelected)
//        }
        val prefs = getSharedPreferences("farmapp_prefs", Context.MODE_PRIVATE)
        val langSelected = prefs.getString("selected_lang", null)

        if (langSelected == null) {
            // First time → dialog
            LanguageDialog(this).showLanguageDialog { lang ->
                prefs.edit().putString("selected_lang", lang).apply()
                selectedLanguage = lang
                recreate() // bas recreate hi karna hai
            }
        } else {
            selectedLanguage = langSelected
        }


        observeWeather()
        observeSoil()
        fetchLocationAndData()
    }

    private fun applylang(context:Context):Context{

        val locale = Locale(selectedLanguage)
        Locale.setDefault(locale);
        val config = android.content.res.Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("farmapp_prefs", Context.MODE_PRIVATE)
        val langSelected = prefs.getString("selected_lang", "en") ?: "en"
        val context = LanguageHelper.setLocale(newBase, langSelected)
        super.attachBaseContext(context)
    }



    class LanguageDialog(private val context: Context) {
        fun showLanguageDialog(onLangSelected: (String) -> Unit) {
            // Naam aur codes map me
            val languages = mapOf(
                "English" to "en",
                "Hindi" to "hi",
                "Korean" to "ko",
                "Malay" to "ms",
                "Nepali" to "ne"
            )

            val displayNames = languages.keys.toTypedArray()

            AlertDialog.Builder(context)
                .setTitle("Choose Language")
                .setItems(displayNames) { _, which ->
                    val selectedName = displayNames[which]
                    val selectedCode = languages[selectedName] ?: "en"
                    onLangSelected(selectedCode) // code pass karega (en, hi, ko…)
                }
                .setCancelable(false)
                .show()
        }
    }



    private fun observeWeather() {
        viewModel.weather.observe(this) { resource ->
            when(resource) {
                is Resource.Loading -> {
                    tvWeatherToday.text = "Loading weather..."
                    tvWeatherTomorrow.text = ""
                }
                is Resource.Success -> {
                    val weather = resource.data
                    tvLocation.text = weather?.location?.name ?: "Unknown location"
                    tvWeatherToday.text = "Today: ${weather?.forecast?.forecastday?.get(0)?.day?.condition?.text}"
                    tvWeatherTomorrow.text = "Tomorrow: ${weather?.forecast?.forecastday?.get(1)?.day?.condition?.text}"
                    tvTemp.text="${weather?.forecast?.forecastday?.get(0)?.day?.avgtemp_c?.inc()}"

                }
                is Resource.Error -> {
                    tvWeatherToday.text = "Error: ${resource.message}"
                    tvWeatherTomorrow.text = ""
                    Toast.makeText(this, "Weather fetch error: ${resource.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun observeSoil() {
        viewModel.soil.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> tvSoilData.text = "Loading soil data..."
                is Resource.Success -> {
                    val soilList = resource.data?.data
                    if (!soilList.isNullOrEmpty()) {
                        tvSoilData.text = soilList.joinToString("\n") { soil ->
                            "Moisture: ${soil.soil_moisture}%, pH: ${soil.soil_ph}, Temp: ${soil.soil_temperature}°C, Fertility: ${soil.fertility}"
                        }
                        Toast.makeText(this, "Soil data fetched!", Toast.LENGTH_SHORT).show()
                    } else {
                        tvSoilData.text = "No soil data"
                        Toast.makeText(this, "No soil data returned.", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Error -> {
                    tvSoilData.text = "Error: ${resource.message}"
                    Toast.makeText(this, "Error fetching soil data: ${resource.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun fetchLocationAndData() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val lat = it.latitude
                val lon = it.longitude
                viewModel.fetchWeather(WEATHER_API_KEY, lat, lon)
               viewModel.fetchSoil(lat, lon, SOIL_API_KEY)
            }
        }
    }
}
