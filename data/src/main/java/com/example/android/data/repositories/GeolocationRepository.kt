package com.example.android.data.repositories

import com.example.android.data.datasources.GeolocationDataSource

class GeolocationRepository(
    private val geolocationDS: GeolocationDataSource
) {

    suspend fun getCountry(latlng: String, mapsKey: String): String = geolocationDS.getCountry(latlng, mapsKey)
}