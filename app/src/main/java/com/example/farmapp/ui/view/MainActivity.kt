
package com.example.farmapp.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
import com.example.farmapp.utils.TranslationHelper

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.mlkit.nl.translate.TranslateLanguage
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


//        val prefs = getSharedPreferences("farmapp_prefs", Context.MODE_PRIVATE)
//        val langSelected = prefs.getString("selected_lang", null)
//
//        if (langSelected == null) {
//            // First time → dialog
//            LanguageDialog(this).showLanguageDialog { lang ->
//                prefs.edit().putString("selected_lang", lang).apply()
//                selectedLanguage = lang
//                recreate() // bas recreate hi karna hai
//            }
//        } else {
//            selectedLanguage = langSelected
//        }
        val prefs = getSharedPreferences("farmapp_prefs", Context.MODE_PRIVATE)
        val langSelected = prefs.getString("selected_lang", null)
        if (langSelected != null) {
            selectedLanguage = langSelected
        }

        // --- Spinner setup ---
        val spinner: Spinner = findViewById(R.id.spinnerLanguage) // Spinner ka ID layout me
        val languages = mapOf(
            "English" to "en",
            "Hindi" to "hi",
            "Khortha" to "kho",
            "Kudmali" to "kud",
            "Nepali" to "ne"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages.keys.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Pre-select current language
        spinner.setSelection(languages.values.indexOf(selectedLanguage))

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val langCode = languages.values.toList()[position]
                if (langCode != selectedLanguage) {
                    selectedLanguage = langCode
                    prefs.edit().putString("selected_lang", langCode).apply()
                    LanguageHelper.setLocale(this@MainActivity, langCode)
                    recreate() // language change apply
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        observeWeather()
        observeSoil()
        fetchLocationAndData()
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
                "Khortha" to "kho",
                "Kudmali" to "kud",
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

                val todayCondition = weather?.forecast?.forecastday?.get(0)?.day?.condition?.text ?: ""
                val tomorrowCondition = if (weather?.forecast?.forecastday?.size ?: 0 > 1) {
                    weather?.forecast?.forecastday?.get(1)?.day?.condition?.text ?: ""
                } else ""
                //val tomorrowCondition = weather?.forecast?.forecastday?.get(1)?.day?.condition?.text ?: ""
                val loc =weather?.location?.name?:""
                // Translator init karo (EN → user selected language)
                val targetLang = when (selectedLanguage) {
                    "hi" -> com.google.mlkit.nl.translate.TranslateLanguage.HINDI
                    //"ne" -> com.google.mlkit.nl.translate.TranslateLanguage.NEPALI
                    "ms" -> com.google.mlkit.nl.translate.TranslateLanguage.MALAY
                    "ko" -> com.google.mlkit.nl.translate.TranslateLanguage.KOREAN
                    else -> com.google.mlkit.nl.translate.TranslateLanguage.ENGLISH
                }

                TranslationHelper.initTranslator(
                    TranslateLanguage.ENGLISH,
                    targetLang,
                    this
                ) {

                    // Translation ready → ab text translate karo
                    TranslationHelper.translate(todayCondition) { translated ->
                        tvWeatherToday.text = "$translated"
                    }
                    TranslationHelper.translate(loc) { translated ->
                        tvLocation.text = "$translated"
                    }

                    TranslationHelper.translate(tomorrowCondition) { translated ->
                        tvWeatherTomorrow.text = "$translated"
                    }
                }

                tvTemp.text = "${weather?.forecast?.forecastday?.get(0)?.day?.avgtemp_c} °C"
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
//                is Resource.Success -> {
//                    val soilList = resource.data?.data
//                    if (!soilList.isNullOrEmpty()) {
//                        tvSoilData.text = soilList.joinToString("\n") { soil ->
//                            "Moisture: ${soil.soil_moisture}%, pH: ${soil.soil_ph}, Temp: ${soil.soil_temperature}°C, Fertility: ${soil.fertility}"
//                        }
//                        Toast.makeText(this, "Soil data fetched!", Toast.LENGTH_SHORT).show()
//                    } else {
//                        tvSoilData.text = "No soil data"
//                        Toast.makeText(this, "No soil data returned.", Toast.LENGTH_LONG).show()
//                    }
//                }
                is Resource.Success -> {
                    val soilList = resource.data?.data
                    if (!soilList.isNullOrEmpty()) {
                        val rawText = soilList.joinToString("\n") { soil ->
                            "Moisture: ${soil.soil_moisture}%, pH: ${soil.soil_ph}, Temp: ${soil.soil_temperature}°C, Fertility: ${soil.fertility}"
                        }

                        val targetLang = when (selectedLanguage) {
                            "hi" -> com.google.mlkit.nl.translate.TranslateLanguage.HINDI
                            //"ne" -> com.google.mlkit.nl.translate.TranslateLanguage.NEPALI
                            "ms" -> com.google.mlkit.nl.translate.TranslateLanguage.MALAY
                            "ko" -> com.google.mlkit.nl.translate.TranslateLanguage.KOREAN
                            else -> com.google.mlkit.nl.translate.TranslateLanguage.ENGLISH
                        }

                        TranslationHelper.initTranslator(
                            TranslateLanguage.ENGLISH, targetLang, this
                        ) {
                            TranslationHelper.translate(rawText) { translated ->
                                tvSoilData.text = translated
                            }
                        }
                    } else {
                        tvSoilData.text = "No soil data"
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
