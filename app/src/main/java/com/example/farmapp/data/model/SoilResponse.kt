package com.example.farmapp.data.model


data class SoilResponse(
    val data: List<AmbeeSoilData>
)

data class AmbeeSoilData(
    val soil_moisture: Double,
    val soil_ph: Double,
    val soil_temperature: Double,
    val fertility: Double,
    val lat: Double,
    val lng: Double
)


