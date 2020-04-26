package com.example.android.data.datasources

interface GeolocationDataSource {

    suspend fun getCountry(latlng: String, mapsKey: String): String
}