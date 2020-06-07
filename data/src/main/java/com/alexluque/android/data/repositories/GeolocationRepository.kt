package com.alexluque.android.data.repositories

import com.alexluque.android.data.datasources.GeolocationDataSource
import retrofit2.Retrofit

class GeolocationRepository(
    private val geolocationDS: GeolocationDataSource
) {

    suspend fun getCountry(retrofit: Retrofit, latlng: String, mapsKey: String): String = geolocationDS.getCountry(retrofit, latlng, mapsKey)
}